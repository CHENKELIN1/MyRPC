package com.ckl.rpc;

import com.ckl.rpc.api.HelloObject;
import com.ckl.rpc.api.HelloService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(HelloObject helloObject) {
      log.info("接收到:"+helloObject.getMsg());
      return "返回值:"+helloObject.getId();
    }
}
