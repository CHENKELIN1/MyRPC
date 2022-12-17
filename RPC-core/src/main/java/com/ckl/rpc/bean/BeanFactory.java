package com.ckl.rpc.bean;

import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.loadbalancer.LoadBalancer;
import com.ckl.rpc.loadbalancer.RandomLoadBalancer;
import com.ckl.rpc.loadbalancer.RoundRobinLoadBalancer;
import com.ckl.rpc.serializer.CommonSerializer;
import com.ckl.rpc.serializer.HessianSerializer;
import com.ckl.rpc.serializer.JsonSerializer;
import com.ckl.rpc.serializer.KryoSerializer;
import com.ckl.rpc.transport.RpcClient;
import com.ckl.rpc.transport.RpcClientProxy;
import com.ckl.rpc.transport.netty.client.NettyClient;
import com.ckl.rpc.transport.socket.client.SocketClient;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BeanFactory implements DefaultConfig{
    private static Map<String,Object> contianer;
    private static RpcClientProxy rpcClientProxy;

     static {
         log.info("初始化BeanFactory...");
        contianer=new HashMap<>();
        LoadBalancer loadBalancer;
        CommonSerializer serializer;
        RpcClient rpcClient;
        switch (DEFAULT_SERIALIZER){
            case JSON:serializer=new JsonSerializer();break;
            case KRYO:serializer=new KryoSerializer();break;
            case HESSIAN:serializer=new HessianSerializer();break;
            default:serializer=new HessianSerializer();
        }
        switch (DEFAULT_LOAD_BALANCE){
            case LOAD_BALANCE_ROUND:loadBalancer=new RoundRobinLoadBalancer();break;
            case LOAD_BALANCE_RANDOM:loadBalancer=new RandomLoadBalancer();break;
            default:loadBalancer=new RandomLoadBalancer();
        }
        switch (DEFAULT_TRANSMISSION){
            case NETTY:rpcClient=new NettyClient(serializer.getCode(),loadBalancer);break;
            case SOCKET:rpcClient=new SocketClient(serializer.getCode(),loadBalancer);break;
            default:rpcClient=new NettyClient(serializer.getCode(),loadBalancer);break;
        }
        rpcClientProxy=new RpcClientProxy(rpcClient);
    }
    public static synchronized <T> T getBean(Class<T> clazz){
        String className= clazz.getCanonicalName();
        if (contianer.containsKey(className)){
            return (T) contianer.get(className);
        }else {
            T object=rpcClientProxy.getProxy(clazz);
            contianer.put(className,object);
            return object;
        }
    }
}
