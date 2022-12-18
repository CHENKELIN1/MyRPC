package com.ckl.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ckl.rpc.enumeration.LoadBalanceType;

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

    static LoadBalancer getByType(LoadBalanceType type){
        switch (type){
            case LOAD_BALANCE_RANDOM:return new RandomLoadBalancer();
            case LOAD_BALANCE_ROUND:return new RoundRobinLoadBalancer();
            case LOAD_BALANCE_ADAPTIVE:return new AdaptiveLoadBalancer();
        }
        return null;
    }
}
