package com.ckl.rpc;

import com.ckl.rpc.api.HelloObject;
import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.client.RpcClientProxy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestClient {
    public static void main(String[] args) {
        RpcClientProxy rpcClientProxy=new RpcClientProxy("127.0.0.1",9000);
        HelloService helloService=rpcClientProxy.getProxy(HelloService.class);
        HelloObject helloObject=new HelloObject(12,"hello");
        String res=helloService.hello(helloObject);
        log.info("success:"+res);
        MyTest myTest=rpcClientProxy.getProxy(MyTest.class);
        String res2= myTest.getData();
        log.info("success:"+res2);
    }
}
