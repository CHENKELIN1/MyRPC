package com.ckl.rpc;

import com.ckl.rpc.serializer.CommonSerializer;

public interface RpcServer {
    void start(int port);
    void setSerializer(CommonSerializer serializer);
}
