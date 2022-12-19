package com.ckl.rpc;

import com.ckl.rpc.api.HelloObject;
import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.bean.BeanFactory;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.enumeration.GroupName;

/**
 * Netty客户端测试
 */
public class TestNettyClient {
    public static void main(String[] args) {
//        远程过程调用接口1
        HelloService helloService = BeanFactory.getBean(HelloService.class, DefaultConfig.DEFAULT_GROUP);
        System.out.println(helloService.hello(new HelloObject(12, "This is a message")));
//        远程过程调用接口2
        MyTest myTest = BeanFactory.getBean(MyTest.class, DefaultConfig.DEFAULT_GROUP);
        System.out.println(myTest.getData());
//        远程过程调用接口2(错误实例)
        MyTest myTest2 = BeanFactory.getBean(MyTest.class, GroupName.GROUP_1);
        System.out.println(myTest2.getData());
    }
}
