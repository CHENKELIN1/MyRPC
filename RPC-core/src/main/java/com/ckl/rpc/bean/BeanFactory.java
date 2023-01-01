package com.ckl.rpc.bean;

import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.config.Init;
import com.ckl.rpc.enumeration.TransmissionType;
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
        if (DEFAULT_TRANSMISSION == TransmissionType.SOCKET) {
            rpcClient = new SocketClient(DEFAULT_SERIALIZER, DEFAULT_LOAD_BALANCE, DEFAULT_COMPRESSER);
        } else if (DEFAULT_TRANSMISSION == TransmissionType.NETTY) {
            rpcClient = new NettyClient(DEFAULT_SERIALIZER, DEFAULT_LOAD_BALANCE, DEFAULT_COMPRESSER);
        } else {
            rpcClient = null;
        }
        rpcClientProxy = new RpcClientProxy(rpcClient);
        log.info("初始化完成:负载均衡器:{},序列化器:{},压缩工具：{}",DEFAULT_LOAD_BALANCE,DEFAULT_SERIALIZER,DEFAULT_COMPRESSER);
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
