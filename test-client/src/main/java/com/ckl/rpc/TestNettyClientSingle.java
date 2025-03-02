package com.ckl.rpc;

import com.ckl.rpc.api.HelloObject;
import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.factory.BeanFactory;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.factory.SingletonFactory;
import com.ckl.rpc.status.ClientMonitor;

/**
 * Netty客户端简单测试用例
 */
public class TestNettyClientSingle {
    public static void main(String[] args) {
//        远程过程调用接口1
        HelloService helloService = BeanFactory.getBean(HelloService.class, DefaultConfig.DEFAULT_GROUP);
        System.out.println(helloService.hello(new HelloObject(12, "This is a message")));
//        远程过程调用接口2
        MyTest myTest = BeanFactory.getBean(MyTest.class, DefaultConfig.DEFAULT_GROUP);
        System.out.println(myTest.getData());
        SingletonFactory.getInstance(ClientMonitor.class).showAllMonitorContent();
    }
}
