package com.ckl.rpc.extension.loadbalance.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.extension.loadbalance.LoadBalancer;

import java.util.List;

/**
 * 轮询负载均衡策略
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private int index = 0;

    @Override
    public synchronized Instance select(List<Instance> instances, RpcRequest rpcRequest) {
        if (index >= instances.size()) {
            index %= instances.size();
        }
        return instances.get(index++);
    }

}
