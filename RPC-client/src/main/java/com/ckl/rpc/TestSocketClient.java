package com.ckl.rpc;

import com.ckl.rpc.api.HelloObject;
import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.serializer.HessianSerializer;
import com.ckl.rpc.socket.client.SocketClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestSocketClient {
    public static void main(String[] args) {
        RpcClient rpcClient=new SocketClient("127.0.0.1",9000);
        rpcClient.setSerializer(new HessianSerializer());
        RpcClientProxy rpcClientProxy=new RpcClientProxy(rpcClient);
        HelloService helloService=rpcClientProxy.getProxy(HelloService.class);
        HelloObject helloObject=new HelloObject(12,"hello");
        String res=helloService.hello(helloObject);
        log.info("success:"+res);
        MyTest myTest=rpcClientProxy.getProxy(MyTest.class);
        String res2= myTest.getData();
        log.info("success:"+res2);
    }
}
