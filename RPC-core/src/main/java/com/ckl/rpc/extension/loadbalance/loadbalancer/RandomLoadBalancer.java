package com.ckl.rpc.extension.loadbalance.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.extension.loadbalance.LoadBalancer;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡策略
 */
public class RandomLoadBalancer implements LoadBalancer {

    @Override
    public Instance select(List<Instance> instances, RpcRequest rpcRequest) {
        return instances.get(new Random().nextInt(instances.size()));
    }

}
