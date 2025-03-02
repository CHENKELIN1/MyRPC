package com.ckl.rpc.extension.loadbalance.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ckl.rpc.annotation.MyRpcExtension;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.enumeration.LoadBalanceType;
import com.ckl.rpc.extension.loadbalance.LoadBalancer;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
@MyRpcExtension(loadBalanceType = LoadBalanceType.LOAD_BALANCE_CONSISTENT_HASH)
public class ConsistentHashLoadBalancer implements LoadBalancer {
    ConcurrentHashMap<String, HashSelector> selectors = new ConcurrentHashMap<>();

    @Override
    public InetSocketAddress select(List<InetSocketAddress> instances, RpcRequest rpcRequest) {
        String key = rpcRequest.getServiceName() + rpcRequest.getGroup();
        HashSelector hashSelector = selectors.get(key);
        if (hashSelector == null) {
            selectors.put(key, new HashSelector(instances));
            hashSelector = selectors.get(key);
        }
        return hashSelector.select(rpcRequest.getRequestId());
    }

    static class HashSelector {
        private final TreeMap<Integer, InetSocketAddress> nodes;

        HashSelector(List<InetSocketAddress> instances) {
            this.nodes = new TreeMap<>();
            for (InetSocketAddress instance : instances) {
                for (int virtual = 0; virtual < 5; virtual++) {
                    String str = instance.getHostName() + instance.getPort() + virtual;
                    int hash = getHash(str);
                    nodes.put(hash, instance);
                }
            }
        }

        //使用FNV1_32_HASH算法计算服务器的Hash值
        private static int getHash(String str) {
            final int p = 16777619;
            int hash = (int) 2166136261L;
            for (int i = 0; i < str.length(); i++)
                hash = (hash ^ str.charAt(i)) * p;
            hash += hash << 13;
            hash ^= hash >> 7;
            hash += hash << 3;
            hash ^= hash >> 17;
            hash += hash << 5;
            if (hash < 0)
                hash = Math.abs(hash);
            return hash;
        }

        public InetSocketAddress select(String key) {
            int hash = getHash(key);
            Map.Entry<Integer, InetSocketAddress> entry = nodes.tailMap(hash, true).firstEntry();
            if (entry == null) {
                entry = nodes.firstEntry();
            }
            return entry.getValue();
        }
    }
}
