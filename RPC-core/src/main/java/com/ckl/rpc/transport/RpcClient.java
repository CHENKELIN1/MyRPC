package com.ckl.rpc.transport;

import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.serializer.CommonSerializer;

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
