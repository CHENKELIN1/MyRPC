package com.ckl.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ckl.rpc.entity.ServerStatus;
import com.ckl.rpc.entity.ServerStatusList;
import com.ckl.rpc.factory.SingletonFactory;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
public class AdaptiveLoadBalancer implements LoadBalancer{
    @Override
    public Instance select(List<Instance> instances) {
        ServerStatusList statusList = SingletonFactory.getInstance(ServerStatusList.class);
        Instance result = null;
        int score =Integer.MAX_VALUE;
        for (int i=0;i<instances.size();i++){
            Instance instance = instances.get(i);
            ServerStatus serverStatus = statusList.getList(getAddress(instance));
            if (serverStatus==null){
                log.info("select not used:"+getAddress(instance));
                return instance;
            }
            int thisScore=getCore(serverStatus);
            result= selectInstance(result,instance,score,thisScore);
            score=flushScore(score,thisScore);
        }
        log.info("select better:"+getAddress(result));
        return result;
    }
    private int getCore(ServerStatus serverStatus){
        return serverStatus.getReceivedCount();
    }
    private InetSocketAddress getAddress(Instance instance){
        return new InetSocketAddress(instance.getIp(),instance.getPort());
    }
    private Instance selectInstance(Instance i1,Instance i2,int s1,int s2){
        return s1<s2?i1:i2;
    }
    private int flushScore(int s1,int s2){
        return Math.min(s1, s2);
    }
}