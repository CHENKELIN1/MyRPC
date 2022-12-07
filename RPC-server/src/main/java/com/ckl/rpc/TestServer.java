package com.ckl.rpc;

import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.server.RpcServer;

public class TestServer {
    public static void main(String[] args) {
        HelloService helloService=new HelloServiceImpl();
        RpcServer rpcServer=new RpcServer();
        rpcServer.register(helloService,9000);
    }
}
