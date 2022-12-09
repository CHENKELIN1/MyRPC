package com.ckl.rpc;

import com.ckl.rpc.annotation.Service;
import com.ckl.rpc.api.MyTest;
@Service
public class MyTestImpl implements MyTest {
    @Override
    public String getData() {
        return "test success";
    }
}
