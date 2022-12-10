package com.ckl.rpc.transport;

import com.ckl.rpc.serializer.CommonSerializer;

/**
 * Rpc服务端
 */
public interface RpcServer {
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    /**
     * 服务端启动
     */
    void start();

    /**
     * 发布服务
     *
     * @param service     服务提供者
     * @param serviceName 服务名称
     */
    <T> void publishService(T service, String serviceName);
}
