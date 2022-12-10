package com.ckl.rpc.server;

import com.ckl.rpc.annotation.Service;
import com.ckl.rpc.api.HelloObject;
import com.ckl.rpc.api.HelloService;
import com.ckl.rpc.api.MyTest;
import lombok.extern.slf4j.Slf4j;

/**
 * HelloService接口实现
 */
@Slf4j
@Service
public class HelloServiceImpl implements HelloService, MyTest {
    @Override
    public String hello(HelloObject helloObject) {
        log.info("接收到:" + helloObject.getMsg());
        return "返回值:" + helloObject.getId();
    }

    @Override
    public String getData() {
        return "myTest:data";
    }
}
