package com.ckl.rpc;

import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.serializer.CommonSerializer;
import com.ckl.rpc.transport.netty.server.NettyServer;

public class TestNettyServer {
    public static void main(String[] args) {
        Thread t1=new Thread(() ->{
            HelloService helloService = new HelloServiceImpl();
            NettyServer server = new NettyServer("127.0.0.1", 9000, CommonSerializer.JSON_SERIALIZER);
            server.publishService(helloService, HelloService.class);
        });
        Thread t2=new Thread(() ->{
            MyTest myTest = new MyTestImpl();
            NettyServer server = new NettyServer("127.0.0.1", 9001, CommonSerializer.JSON_SERIALIZER);
            server.publishService(myTest, MyTest.class);
        });
        t1.start();
        t2.start();
    }
}
