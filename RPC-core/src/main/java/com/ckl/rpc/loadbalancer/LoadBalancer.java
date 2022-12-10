package com.ckl.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡器
 */
public interface LoadBalancer {
    /**
     * 选择服务实例
     *
     * @param instances 所有服务实例
     * @return 服务实例
     */
    Instance select(List<Instance> instances);
}
