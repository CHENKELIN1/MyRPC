package com.ckl.rpc.transport;

import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.serializer.CommonSerializer;

public interface RpcClient {
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    Object sendRequest(RpcRequest rpcRequest);
}
