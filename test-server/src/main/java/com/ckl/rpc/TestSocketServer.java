package com.ckl.rpc;

import com.ckl.rpc.annotation.MyRpcServiceScan;
import com.ckl.rpc.transport.common.server.RpcServer;
import com.ckl.rpc.transport.socket.server.SocketServer;

/**
 * Socket服务端测试
 */
@MyRpcServiceScan
public class TestSocketServer {
    public static void main(String[] args) {
        RpcServer server = new SocketServer("127.0.0.1", 9998);
        server.start();
    }
}
