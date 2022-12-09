package com.ckl.rpc;

import com.ckl.rpc.api.HelloObject;
import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.serializer.CommonSerializer;
import com.ckl.rpc.transport.RpcClient;
import com.ckl.rpc.transport.RpcClientProxy;
import com.ckl.rpc.transport.netty.client.NettyClient;

/**
 * Netty客户端测试
 */
public class TestNettyClient {
    public static void main(String[] args) {
//        创建Netty客户端
        RpcClient rpcClient = new NettyClient(CommonSerializer.JSON_SERIALIZER);
//        创建rpc客户端代理
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
//        远程过程调用接口1
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        System.out.println(helloService.hello(new HelloObject(12, "This is a message")));
//        远程过程调用接口2
        MyTest myTest = rpcClientProxy.getProxy(MyTest.class);
        System.out.println(myTest.getData());
    }
}
