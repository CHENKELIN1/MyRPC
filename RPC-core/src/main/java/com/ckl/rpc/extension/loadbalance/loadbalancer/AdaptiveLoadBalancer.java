package com.ckl.rpc.extension.loadbalance.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ckl.rpc.annotation.MyRpcExtension;
import com.ckl.rpc.entity.ClientMonitorContent;
import com.ckl.rpc.entity.RpcRequest;
import com.ckl.rpc.enumeration.LoadBalanceType;
import com.ckl.rpc.extension.loadbalance.LoadBalancer;
import com.ckl.rpc.factory.SingletonFactory;
import com.ckl.rpc.status.ClientMonitor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 自适应负载均衡器
 */
@Slf4j
@MyRpcExtension(loadBalanceType = LoadBalanceType.LOAD_BALANCE_ADAPTIVE)
public class AdaptiveLoadBalancer implements LoadBalancer {
    /**
     * 选择服务实例
     *
     * @param instances  所有服务实例
     * @param rpcRequest
     * @return 服务实例
     */
    @Override
    public InetSocketAddress select(List<InetSocketAddress> instances, RpcRequest rpcRequest) {
//        获取监控器
        ClientMonitor clientMonitor = SingletonFactory.getInstance(ClientMonitor.class);
//        初始化结果
        InetSocketAddress result = null;
        int score = Integer.MAX_VALUE;
//        遍历所有接口
        for (int i = 0; i < instances.size(); i++) {
            InetSocketAddress instance = instances.get(i);
//            获取监控内容
            ClientMonitorContent clientMonitorContent = clientMonitor.getMonitorContent(getAddress(instance));
//            为空则表示未使用过，则直接返回
            if (clientMonitorContent == null) {
//                log.info("select not used:" + getAddress(instance));
                return instance;
            }
//            计算得分
            int thisScore = getScore(clientMonitorContent);
//            更新最佳值
            result = selectInstance(result, instance, score, thisScore);
            score = flushScore(score, thisScore);
        }
//        log.info("select better:" + getAddress(result));
        return result;
    }

    /**
     * 计算得分
     *
     * @param clientMonitorContent 监控内容
     * @return 得分
     */
    private int getScore(ClientMonitorContent clientMonitorContent) {
        return clientMonitorContent.getStatus().getReceivedCount();
    }

    /**
     * 获取地址
     *
     * @param instance 接口实例
     * @return 地址
     */
    private InetSocketAddress getAddress(InetSocketAddress instance) {
        return new InetSocketAddress(instance.getHostName(), instance.getPort());
    }

    /**
     * 自定义策略
     *
     * @param i1 实例1
     * @param i2 实例2
     * @param s1 实例1得分
     * @param s2 实例2得分
     * @return 选择结果
     */
    private InetSocketAddress selectInstance(InetSocketAddress i1, InetSocketAddress i2, int s1, int s2) {
        return s1 < s2 ? i1 : i2;
    }

    /**
     * 更新得分
     *
     * @param s1 实例1得分
     * @param s2 实例2得分
     * @return 结果
     */
    private int flushScore(int s1, int s2) {
        return Math.min(s1, s2);
    }
}
