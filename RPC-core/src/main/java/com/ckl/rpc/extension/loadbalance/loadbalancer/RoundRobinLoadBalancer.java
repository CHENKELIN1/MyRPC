package com.ckl.rpc.extension.loadbalance.loadbalancer;

import com.ckl.rpc.annotation.MyRpcExtension;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.enumeration.LoadBalanceType;
import com.ckl.rpc.extension.loadbalance.LoadBalancer;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 轮询负载均衡策略
 */
@MyRpcExtension(loadBalanceType = LoadBalanceType.LOAD_BALANCE_ROUND)
public class RoundRobinLoadBalancer implements LoadBalancer {

    private int index = 0;

    @Override
    public synchronized InetSocketAddress select(List<InetSocketAddress> instances, RpcRequest rpcRequest) {
        if (index >= instances.size()) {
            index %= instances.size();
        }
        return instances.get(index++);
    }

}
