package com.ckl.rpc.server;

import com.ckl.rpc.annotation.MyRpcService;
import com.ckl.rpc.api.MyTest;

/**
 * MyTest 接口实现
 */
@MyRpcService
public class MyTestImpl implements MyTest {
    @Override
    public String getData() {
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return "my test success!!!";
    }
}
