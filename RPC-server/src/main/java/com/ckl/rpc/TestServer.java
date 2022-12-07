package com.ckl.rpc;

import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.registry.DefaultServiceRegistry;
import com.ckl.rpc.registry.ServiceRegistry;
import com.ckl.rpc.server.RpcServer;

public class TestServer {
    public static void main(String[] args) {
        HelloService helloService=new HelloServiceImpl();
        MyTest myTest=new MyTestImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        serviceRegistry.register(myTest);
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(9000);
    }
}
