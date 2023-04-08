package com.ckl.rpc.extension.loadbalance.loadbalancer;

import com.ckl.rpc.annotation.MyRpcExtension;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.enumeration.LoadBalanceType;
import com.ckl.rpc.extension.loadbalance.LoadBalancer;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡策略
 */
@MyRpcExtension(loadBalanceType = LoadBalanceType.LOAD_BALANCE_RANDOM)
public class RandomLoadBalancer implements LoadBalancer {

    @Override
    public InetSocketAddress select(List<InetSocketAddress> instances, RpcRequest rpcRequest) {
        return instances.get(new Random().nextInt(instances.size()));
    }

}
