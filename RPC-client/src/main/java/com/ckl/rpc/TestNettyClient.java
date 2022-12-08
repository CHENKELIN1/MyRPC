package com.ckl.rpc;

import com.ckl.rpc.api.HelloObject;
import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.serializer.HessianSerializer;
import com.ckl.rpc.transport.netty.client.NettyClient;

public class TestNettyClient {
    public static void main(String[] args) {
        RpcClient rpcClient = new NettyClient();
        rpcClient.setSerializer(new HessianSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
