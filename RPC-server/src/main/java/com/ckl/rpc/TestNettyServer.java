package com.ckl.rpc;

import com.ckl.rpc.annotation.ServiceScan;
import com.ckl.rpc.serializer.CommonSerializer;
import com.ckl.rpc.transport.netty.server.NettyServer;

@ServiceScan
public class TestNettyServer {
    public static void main(String[] args) {
        RpcServer server = new NettyServer("127.0.0.1", 9000, CommonSerializer.JSON_SERIALIZER);
        server.start();
    }
}
