package com.ckl.rpc.server;

import com.ckl.rpc.annotation.MyRpcService;
import com.ckl.rpc.api.MyTest;
import com.ckl.rpc.exception.RpcException;

/**
 * MyTest接口实现
 */
@MyRpcService
public class MyTestImpl implements MyTest {
    @Override
    public String getData() {
        int a=1/0;
        return "success";
    }
}
