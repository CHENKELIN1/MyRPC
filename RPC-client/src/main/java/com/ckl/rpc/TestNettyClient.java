package com.ckl.rpc;

import com.ckl.rpc.api.HelloObject;
import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.netty.client.NettyClient;
import com.ckl.rpc.serializer.HessianSerializer;

public class TestNettyClient {
    public static void main(String[] args) {
        RpcClient rpcClient = new NettyClient("127.0.0.1", 9000);
        rpcClient.setSerializer(new HessianSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
