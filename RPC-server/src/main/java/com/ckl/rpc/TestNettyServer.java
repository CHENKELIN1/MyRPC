package com.ckl.rpc;

import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.netty.server.NettyServer;
import com.ckl.rpc.registry.DefaultServiceRegistry;
import com.ckl.rpc.registry.ServiceRegistry;

public class TestNettyServer {
    public static void main(String[] args) {
        HelloService helloService=new HelloServiceImpl();
        ServiceRegistry serviceRegistry=new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        NettyServer nettyServer=new NettyServer();
        nettyServer.start(9000);
    }
}
