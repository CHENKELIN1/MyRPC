package com.ckl.rpc.hook;

import com.ckl.rpc.factory.ThreadPoolFactory;
import com.ckl.rpc.registry.ServiceRegistry;
import com.ckl.rpc.registry.ServiceRegistryImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * 关闭连接钩子函数
 */
@Slf4j
public class ShutdownHook {
    private static final ShutdownHook shutdownHook = new ShutdownHook();

    /**
     * 获取钩子函数
     *
     * @return
     */
    public static ShutdownHook getShutdownHook() {
        return shutdownHook;
    }

    /**
     * 添加注销所有服务对钩子函数
     */
    public void addClearAllHook(ServiceRegistry registry) {
        log.info("关闭后将自动注销所有服务");
//        添加钩子函数
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            registry.clearRegistry();
//            关闭所有线程池
            ThreadPoolFactory.shutDownAll();
        }));
    }
}
