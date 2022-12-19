package com.ckl.rpc.server;

import com.ckl.rpc.annotation.MyRpcService;
import com.ckl.rpc.api.HelloObject;
import com.ckl.rpc.api.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * HelloService接口实现
 */
@Slf4j
@MyRpcService
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(HelloObject helloObject) {
        log.info("接收到:" + helloObject.getMsg());
        return "返回值:" + helloObject.getId();
    }
}
