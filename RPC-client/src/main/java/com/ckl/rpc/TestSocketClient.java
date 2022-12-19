package com.ckl.rpc;

import com.ckl.rpc.api.HelloObject;
import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.bean.BeanFactory;
import com.ckl.rpc.enumeration.GroupName;
import com.ckl.rpc.factory.SingletonFactory;
import com.ckl.rpc.status.ServerMonitor;
import lombok.extern.slf4j.Slf4j;

/**
 * Socket客户端测试
 */
@Slf4j
public class TestSocketClient {
    public static void main(String[] args) {
//         测试接口1
        HelloService helloService = BeanFactory.getBean(HelloService.class, GroupName.GROUP_0);
        helloService.hello(new HelloObject(10, "hello socket client"));
//        测试接口2
        MyTest myTest = BeanFactory.getBean(MyTest.class, GroupName.GROUP_0);
        myTest.getData();
        SingletonFactory.getInstance(ServerMonitor.class).showAllMonitorContent();
    }
}
