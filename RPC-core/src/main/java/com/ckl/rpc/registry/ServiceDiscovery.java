package com.ckl.rpc.registry;

import com.ckl.rpc.entity.RpcRequest;

import java.net.InetSocketAddress;

/**
 * 服务发现
 */
public interface ServiceDiscovery {
    /**
     * 查找服务
     *
     * @param rpcRequest 服务名称
     * @return 服务对应到socket地址
     */
    InetSocketAddress lookupService(RpcRequest rpcRequest);
}
