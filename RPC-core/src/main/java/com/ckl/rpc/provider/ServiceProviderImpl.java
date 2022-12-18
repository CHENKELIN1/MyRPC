package com.ckl.rpc.provider;

import com.ckl.rpc.entity.Register;
import com.ckl.rpc.enumeration.RpcError;
import com.ckl.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务提供者实现
 */
@Slf4j
public class ServiceProviderImpl implements ServiceProvider {
    //    存储服务名与提供服务的对象的对应关系
    private static final Map<Register, Object> serviceMap = new ConcurrentHashMap<>();

    /**
     * 添加服务提供者
     *
     * @param service     服务提供对象
     * @param serviceName 服务名称
     * @param <T>
     */
    @Override
    public <T> void addServiceProvider(T service, String serviceName,String group) {
        Register register = new Register(serviceName, group);
//        已被注册，则跳过
        if (serviceMap.containsKey(register)) return;
//        添加映射关系到map
        serviceMap.put(register, service);
        log.info("向接口: {} 注册服务: {}", service.getClass().getInterfaces(),new Register(serviceName,group));
    }

    /**
     * 获取服务对象
     *
     * @param serviceName 服务名称
     * @return
     */
    @Override
    public Object getServiceProvider(String serviceName,String group) {
        Object service = serviceMap.get(new Register(serviceName,group));
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
