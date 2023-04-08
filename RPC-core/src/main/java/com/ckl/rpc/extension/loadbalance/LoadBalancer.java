package com.ckl.rpc.extension.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ckl.rpc.entity.RpcRequest;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 负载均衡器
 */
public interface LoadBalancer {
    /**
     * 选择服务实例
     *
     * @param instances 所有服务实例
     * @return 服务实例
     */
    InetSocketAddress select(List<InetSocketAddress> instances, RpcRequest rpcRequest);
}
