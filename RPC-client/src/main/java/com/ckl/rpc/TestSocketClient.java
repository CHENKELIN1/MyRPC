package com.ckl.rpc;

import com.ckl.rpc.api.HelloObject;
import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.serializer.HessianSerializer;
import com.ckl.rpc.transport.socket.client.SocketClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestSocketClient {
    public static void main(String[] args) {
        RpcClient rpcClient = new SocketClient();
        rpcClient.setSerializer(new HessianSerializer());
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject helloObject = new HelloObject(12, "hello");
        String res = helloService.hello(helloObject);
        System.out.println(res);
        MyTest myTest = rpcClientProxy.getProxy(MyTest.class);
        String res2 = myTest.getData();
        System.out.println(res2);
    }
}
