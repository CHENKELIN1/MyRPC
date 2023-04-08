package com.ckl.rpc.registry;

import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.enumeration.RpcError;
import com.ckl.rpc.exception.RpcException;
import com.ckl.rpc.extension.loadbalance.LoadBalancer;
import com.ckl.rpc.extension.registry.RegistryHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
public class ServiceDiscoveryImpl implements ServiceDiscovery {
    private final LoadBalancer loadBalancer;
    private final RegistryHandler registryHandler;

    public ServiceDiscoveryImpl(LoadBalancer loadBalancer, RegistryHandler handler) {
        this.loadBalancer = loadBalancer;
        this.registryHandler = handler;

    }

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        try {
//            获取所有服务
            List<InetSocketAddress> instances = registryHandler.getAllInstance(rpcRequest.getServiceName(), rpcRequest.getGroup());
            if (instances.size() == 0) {
                log.error("找不到对应的服务: " + rpcRequest.getServiceName());
                throw new RpcException(RpcError.SERVICE_NOT_FOUND);
            }
//            选取一个服务实例
            return loadBalancer.select(instances, rpcRequest);
        } catch (Exception e) {
            log.error("获取服务时有错误发生:", e);
        }
        return null;
    }

}
