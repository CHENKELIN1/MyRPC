package com.ckl.rpc;

import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.bean.BeanFactory;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.status.ServerMonitor;
import com.ckl.rpc.factory.SingletonFactory;

import java.util.Random;

public class TestNettyClientMultithreading {
    public static void main(String[] args) throws InterruptedException {
        class test implements Runnable{

            @Override
            public void run() {
                for (int i=0;i<100;i++){
                    try {
                        int r=new Random().nextInt()%10;
                        Thread.sleep(r>0?r*1000:-1*r*5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    BeanFactory.getBean(MyTest.class, DefaultConfig.DEFAULT_GROUP).getData();
                }
            }
        }
        Thread[] threads=new Thread[100];
        for (int i=0;i<100;i++){
            threads[i]=new Thread(new test());
            threads[i].start();
        }
        while (true){
            Thread.sleep(1000*5);
            SingletonFactory.getInstance(ServerMonitor.class).showAllMonitorContent();
        }
    }
}
