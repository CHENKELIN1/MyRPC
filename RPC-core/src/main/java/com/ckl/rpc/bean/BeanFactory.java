package com.ckl.rpc.bean;

import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.config.Init;
import com.ckl.rpc.limiter.CounterLimitHandler;
import com.ckl.rpc.transport.RpcClient;
import com.ckl.rpc.transport.RpcClientProxy;
import com.ckl.rpc.transport.netty.client.NettyClient;
import com.ckl.rpc.transport.socket.client.SocketClient;
import lombok.extern.slf4j.Slf4j;

/**
 * 接口工厂
 */
@Slf4j
public class BeanFactory implements DefaultConfig {
    private static final RpcClientProxy rpcClientProxy;

    /**
     * 初始化
     */
    static {
        Init.init();
        log.info("初始化BeanFactory...");
        RpcClient rpcClient;
        switch (DEFAULT_TRANSMISSION) {
            case SOCKET:
                rpcClient = new SocketClient();
                break;
            default:
                rpcClient = new NettyClient();
                break;
        }
        rpcClientProxy = new RpcClientProxy(rpcClient, new CounterLimitHandler(CLIENT_LIMIT_COUNT));
        log.info("初始化完成:lb:" + DEFAULT_LOAD_BALANCE);
    }

    /**
     * 获取服务对象
     *
     * @param clazz 服务类型
     * @param group 业务组
     * @return 服务对象
     */
    public static <T> T getBean(Class<T> clazz, String group) {
        return rpcClientProxy.getProxy(clazz, group);
    }
}
