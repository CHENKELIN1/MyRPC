package com.ckl.rpc;

import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.serializer.HessianSerializer;
import com.ckl.rpc.transport.netty.server.NettyServer;

public class TestNettyServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer server = new NettyServer("127.0.0.1", 9000);
        server.setSerializer(new HessianSerializer());
        server.publishService(helloService, HelloService.class);
    }
}
