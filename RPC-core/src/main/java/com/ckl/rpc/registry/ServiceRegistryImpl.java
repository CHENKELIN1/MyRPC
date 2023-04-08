package com.ckl.rpc.registry;

import com.ckl.rpc.extension.registry.RegistryHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class ServiceRegistryImpl implements ServiceRegistry {

    private final RegistryHandler handler;

    public ServiceRegistryImpl(RegistryHandler handler) {
        this.handler = handler;
    }


    @Override
    public void register(String serviceName, String group, InetSocketAddress inetSocketAddress) {
        try {
            handler.registerService(serviceName, group, inetSocketAddress);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
