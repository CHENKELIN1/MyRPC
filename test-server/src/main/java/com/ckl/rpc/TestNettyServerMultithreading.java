package com.ckl.rpc;

import com.ckl.rpc.annotation.MyRpcServiceScan;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.transport.common.server.RpcServer;
import com.ckl.rpc.transport.netty.server.NettyServer;

/**
 * Netty服务端多线程测试用例TestNettyClientMultithreading
 */
@MyRpcServiceScan(DefaultConfig.DEFAULT_PACKAGE)
public class TestNettyServerMultithreading {
    public static void main(String[] args) throws InterruptedException {
        class test implements Runnable {
            int count;

            public test(int i) {
                this.count = i;
            }

            @Override
            public void run() {
                RpcServer server = new NettyServer("127.0.0.1", 9000 + count);
                server.start();
            }
        }
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(new test(i), "t" + i);
            threads[i].start();
        }
        threads[0].join();
    }
}
