package com.ckl.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.enumeration.RpcError;
import com.ckl.rpc.exception.RpcException;
import com.ckl.rpc.extension.loadbalance.LoadBalancer;
import com.ckl.rpc.extension.loadbalance.loadbalancer.RandomLoadBalancer;
import com.ckl.rpc.util.NacosUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 服务发现
 */
@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery {
    //    负载均衡器
    private final LoadBalancer loadBalancer;

    public NacosServiceDiscovery(LoadBalancer loadBalancer) {
        if (loadBalancer == null) this.loadBalancer = new RandomLoadBalancer();
        else this.loadBalancer = loadBalancer;
    }

    /**
     * 查找服务地址
     *
     * @param rpcRequest 服务名称
     * @return
     */
    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        try {
//            获取所有服务
            List<Instance> instances = NacosUtil.getAllInstance(rpcRequest.getServiceName(), rpcRequest.getGroup());
            if (instances.size() == 0) {
                log.error("找不到对应的服务: " + rpcRequest.getServiceName());
                throw new RpcException(RpcError.SERVICE_NOT_FOUND);
            }
//            选取一个服务实例
            Instance instance = loadBalancer.select(instances, rpcRequest);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            log.error("获取服务时有错误发生:", e);
        }
        return null;
    }
}
