package com.ckl.rpc;

import com.ckl.rpc.annotation.MyRpcServiceScan;
import com.ckl.rpc.transport.common.server.RpcServer;
import com.ckl.rpc.transport.netty.server.NettyServer;

/**
 * Netty服务端简单测试用例
 */
@MyRpcServiceScan
public class TestNettyServerSingle {
    public static void main(String[] args) {
        RpcServer server = new NettyServer("127.0.0.1", 9990);
        server.start();
    }
}
