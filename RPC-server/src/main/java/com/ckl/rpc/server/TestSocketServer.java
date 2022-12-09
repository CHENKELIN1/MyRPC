package com.ckl.rpc.server;

import com.ckl.rpc.serializer.CommonSerializer;
import com.ckl.rpc.transport.RpcServer;
import com.ckl.rpc.transport.socket.server.SocketServer;

/**
 * Socket服务端测试
 */
public class TestSocketServer {
    public static void main(String[] args) {
        RpcServer server = new SocketServer("127.0.0.1", 9998, CommonSerializer.JSON_SERIALIZER);
        server.start();
    }
}
