package com.ckl.rpc;

import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.registry.DefaultServiceRegistry;
import com.ckl.rpc.registry.ServiceRegistry;
import com.ckl.rpc.serializer.HessianSerializer;
import com.ckl.rpc.socket.server.SocketServer;

public class TestSocketServer {
    public static void main(String[] args) {
        HelloService helloService=new HelloServiceImpl();
        MyTest myTest=new MyTestImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        serviceRegistry.register(myTest);
        SocketServer socketServer = new SocketServer(serviceRegistry);
        socketServer.setSerializer(new HessianSerializer());
        socketServer.start(9999);
    }
}
