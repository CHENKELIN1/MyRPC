package com.ckl.rpc.server;

import com.ckl.rpc.annotation.MyRpcService;
import com.ckl.rpc.api.MyTest;

@MyRpcService
public class MyTestImpl implements MyTest {
    @Override
    public String getData() {
//        HelloService helloService = BeanFactory.getBean(HelloService.class, GroupName.GROUP_0);
//        System.out.println(helloService.hello(new HelloObject(1, "hello myTest")));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "my test success!!!";
    }
}
