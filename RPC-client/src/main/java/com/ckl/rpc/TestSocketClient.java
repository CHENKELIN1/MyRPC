package com.ckl.rpc;

import com.ckl.rpc.api.HelloObject;
import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.transport.RpcClientProxy;
import com.ckl.rpc.transport.socket.client.SocketClient;
import lombok.extern.slf4j.Slf4j;

/**
 * Socket客户端测试
 */
@Slf4j
public class TestSocketClient {
    public static void main(String[] args) {

        SocketClient rpcClient = new SocketClient(DefaultConfig.DEFAULT_LOAD_BALANCE);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
//         测试接口1
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class,DefaultConfig.DEFAULT_GROUP);
        HelloObject helloObject = new HelloObject(12, "hello");
        String res = helloService.hello(helloObject);
        System.out.println(res);
//        测试接口2
        MyTest myTest = rpcClientProxy.getProxy(MyTest.class,DefaultConfig.DEFAULT_GROUP);
        String res2 = myTest.getData();
        System.out.println(res2);
    }
}
