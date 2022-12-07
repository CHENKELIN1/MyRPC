package com.ckl.rpc;

import com.ckl.rpc.api.MyTest;

public class MyTestImpl implements MyTest {
    @Override
    public String getData() {
        return "test success";
    }
}
