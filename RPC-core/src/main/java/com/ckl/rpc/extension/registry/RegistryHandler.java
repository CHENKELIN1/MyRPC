package com.ckl.rpc.extension.registry;

import com.alibaba.nacos.api.exception.NacosException;

import java.net.InetSocketAddress;
import java.util.List;

public interface RegistryHandler {

    /**
     * 注册服务
     *
     * @param serviceName 服务名称
     * @param address     服务地址
     * @throws NacosException
     */
    void registerService(String serviceName, String group, InetSocketAddress address) throws Exception;

    /**
     * 获取所有实例
     *
     * @param serviceName
     * @return
     * @throws NacosException
     */
    List<InetSocketAddress> getAllInstance(String serviceName, String group) throws Exception;

    /**
     * 清空所有注册表
     */
    void clearRegistry() throws Exception;
}
