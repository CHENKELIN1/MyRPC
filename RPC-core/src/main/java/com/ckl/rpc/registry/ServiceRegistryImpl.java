package com.ckl.rpc.registry;

import com.ckl.rpc.entity.Register;
import com.ckl.rpc.extension.registry.RegistryHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ServiceRegistryImpl implements ServiceRegistry {
    //    服务名
    private static final Set<Register> service = new HashSet<>();
    //    服务端地址
    private static InetSocketAddress address;
    private final RegistryHandler handler;

    public ServiceRegistryImpl(RegistryHandler handler) {
        this.handler = handler;
    }


    @Override
    public void register(String serviceName, String group, InetSocketAddress inetSocketAddress) {
        try {
            handler.registerService(serviceName, group, inetSocketAddress);
            service.add(new Register(serviceName, group));
            ServiceRegistryImpl.address = inetSocketAddress;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearRegistry() {
        try {
            handler.clearRegistry(service,address);
        } catch (Exception e) {
            log.error("服务注销失败：{}",e.getMessage());
        }
    }
}
