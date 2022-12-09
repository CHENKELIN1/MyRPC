package com.ckl.rpc;

import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.serializer.CommonSerializer;
import com.ckl.rpc.transport.socket.server.SocketServer;

public class TestSocketServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        SocketServer socketServer = new SocketServer("127.0.0.1", 9998, CommonSerializer.KRYO_SERIALIZER);
        socketServer.publishService(helloService, HelloService.class);
    }
}
