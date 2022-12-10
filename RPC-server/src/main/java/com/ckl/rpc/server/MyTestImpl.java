package com.ckl.rpc.server;

import com.ckl.rpc.annotation.Service;
import com.ckl.rpc.api.MyTest;

/**
 * MyTest接口实现
 */
@Service
public class MyTestImpl implements MyTest {
    @Override
    public String getData() {
        return "test success";
    }
}
