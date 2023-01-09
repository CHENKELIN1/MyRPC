package com.ckl.rpc;

import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.bean.BeanFactory;
import com.ckl.rpc.enumeration.GroupName;
import com.ckl.rpc.factory.SingletonFactory;
import com.ckl.rpc.status.ServerMonitor;

import java.util.Random;

/**
 * 客户端测试方法
 */
public class TestFunction {
    public void test() {
        while (true) {
            BeanFactory.getBean(MyTest.class, GroupName.GROUP_0).getData();
            SingletonFactory.getInstance(ServerMonitor.class).showAllMonitorContent();
            int r = new Random().nextInt() % 10;
            try {
                Thread.sleep(r > 0 ? r * 1000 : -1 * r * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
