package com.ckl.rpc.extension.registry.registryHandler;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ckl.rpc.annotation.MyRpcExtension;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.entity.Register;
import com.ckl.rpc.enumeration.RegistryCode;
import com.ckl.rpc.enumeration.RpcError;
import com.ckl.rpc.exception.RpcException;
import com.ckl.rpc.extension.registry.RegistryHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@MyRpcExtension(registryCode = RegistryCode.NACOS)
public class NacosHandler implements RegistryHandler {
    private static final NamingService namingService;

    static {
        namingService = getNacosNamingService();
    }

    public static NamingService getNacosNamingService() {
        try {
            return NamingFactory.createNamingService(DefaultConfig.DEFAULT_NACOS_SERVER_ADDRESS);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    @Override
    public void registerService(String serviceName, String group, InetSocketAddress address) throws Exception {
        namingService.registerInstance(serviceName, group, address.getHostName(), address.getPort());
    }

    @Override
    public List<InetSocketAddress> getAllInstance(String serviceName, String group) throws Exception {
        List<Instance> instances = namingService.getAllInstances(serviceName, group);
        return instances.stream().map(e -> new InetSocketAddress(e.getIp(), e.getPort())).collect(Collectors.toList());
    }

    @Override
    public void clearRegistry(Set<Register> service, InetSocketAddress address) throws Exception {
        if (!service.isEmpty() && address != null) {
            String host = address.getHostName();
            int port = address.getPort();
            for (Register register : service) {
                try {
                    namingService.deregisterInstance(register.getServiceName(), register.getGroup(), host, port);
                    log.info("注销服务:service:{},address:{}",register.getServiceName()+register.getGroup(),host+port);
                } catch (NacosException e) {
                    log.error("注销服务 {},{} 失败", register.getServiceName(), register.getGroup(), e);
                }
            }
        }
    }
}
