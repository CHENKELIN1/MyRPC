package com.ckl.rpc;

import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.serializer.CommonSerializer;

public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);

    void setSerializer(CommonSerializer serializer);
}
