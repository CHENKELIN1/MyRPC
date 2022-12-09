package com.ckl.rpc.server;

import com.ckl.rpc.annotation.ServiceScan;
import com.ckl.rpc.serializer.CommonSerializer;
import com.ckl.rpc.transport.RpcServer;
import com.ckl.rpc.transport.netty.server.NettyServer;

/**
 * Netty服务端测试
 */
@ServiceScan
public class TestNettyServer {
    public static void main(String[] args) {
        RpcServer server = new NettyServer("127.0.0.1", 9000, CommonSerializer.JSON_SERIALIZER);
        server.start();
    }
}
