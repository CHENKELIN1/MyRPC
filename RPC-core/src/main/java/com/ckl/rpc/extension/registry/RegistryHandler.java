package com.ckl.rpc.extension.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.ckl.rpc.entity.Register;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;

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
    void clearRegistry(Set<Register> service, InetSocketAddress address) throws Exception;
}
