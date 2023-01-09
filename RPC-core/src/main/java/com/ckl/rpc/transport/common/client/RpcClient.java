package com.ckl.rpc.transport.common.client;

import com.ckl.rpc.entity.RpcRequest;

/**
 * Rpc客户端
 */
public interface RpcClient {

    /**
     * 发送请求
     *
     * @param rpcRequest
     * @return
     */
    Object sendRequest(RpcRequest rpcRequest);
}
