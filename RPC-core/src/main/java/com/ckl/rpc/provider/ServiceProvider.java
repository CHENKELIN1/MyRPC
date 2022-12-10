package com.ckl.rpc.provider;

/**
 * 服务提供者
 */
public interface ServiceProvider {
    /**
     * 添加服务提供者
     *
     * @param service     服务提供对象
     * @param serviceName 服务名称
     */
    <T> void addServiceProvider(T service, String serviceName);

    /**
     * 获取服务提供者
     *
     * @param serviceName 服务名称
     * @return 服务提供者对象
     */
    Object getServiceProvider(String serviceName);
}
