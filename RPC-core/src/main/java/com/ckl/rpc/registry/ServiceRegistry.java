package com.ckl.rpc.registry;

import java.net.InetSocketAddress;

/**
 * 服务注册
 */
public interface ServiceRegistry {
    /**
     * 将一个服务注册进注册表
     *
     * @param serviceName       服务名称
     * @param group
     * @param inetSocketAddress 提供服务的地址
     */
    void register(String serviceName, String group, InetSocketAddress inetSocketAddress);
    void clearRegistry();
}
