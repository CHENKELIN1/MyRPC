package com.ckl.rpc.extension;

import com.ckl.rpc.enumeration.CompressType;
import com.ckl.rpc.enumeration.LimiterType;
import com.ckl.rpc.enumeration.LoadBalanceType;
import com.ckl.rpc.enumeration.SerializerCode;
import com.ckl.rpc.extension.compress.Compresser;
import com.ckl.rpc.extension.compress.compresser.GZipCompresser;
import com.ckl.rpc.extension.limit.Limiter;
import com.ckl.rpc.extension.limit.limiter.CounterLimiter;
import com.ckl.rpc.extension.loadbalance.LoadBalancer;
import com.ckl.rpc.extension.loadbalance.loadbalancer.AdaptiveLoadBalancer;
import com.ckl.rpc.extension.loadbalance.loadbalancer.RandomLoadBalancer;
import com.ckl.rpc.extension.loadbalance.loadbalancer.RoundRobinLoadBalancer;
import com.ckl.rpc.extension.serialize.Serializer;
import com.ckl.rpc.extension.serialize.serializer.HessianSerializer;
import com.ckl.rpc.extension.serialize.serializer.JsonSerializer;
import com.ckl.rpc.extension.serialize.serializer.KryoSerializer;

public class ExtensionFactory {
    public static <T> T getExtension(Class<T> clazz, Object o) {
        String name = clazz.getName();
        if (name.equals(Limiter.class.getName()) && o instanceof LimiterType) {
            return (T) handleLimit((LimiterType) o);
        }
        if (name.equals(LoadBalancer.class.getName()) && o instanceof LoadBalanceType) {
            return (T) handleLoadBalance((LoadBalanceType) o);
        }
        if (name.equals(Serializer.class.getName())) {
            int code = 0;
            if (o instanceof SerializerCode) {
                code = ((SerializerCode) o).getCode();
            }
            return (T) handleSerializer(code);
        }
        if (name.equals(Compresser.class.getName())) {
            int code = 0;
            if (o instanceof CompressType) {
                code = ((CompressType) o).getCode();
            }
            return (T) handleCompress(code);
        }
        return null;
    }

    private static Limiter handleLimit(LimiterType o) {
        if (o == LimiterType.COUNTER) {
            return new CounterLimiter();
        }
        return null;
    }

    private static LoadBalancer handleLoadBalance(LoadBalanceType o) {
        if (o == LoadBalanceType.LOAD_BALANCE_RANDOM) {
            return new RandomLoadBalancer();
        }
        if (o == LoadBalanceType.LOAD_BALANCE_ROUND) {
            return new RoundRobinLoadBalancer();
        }
        if (o == LoadBalanceType.LOAD_BALANCE_ADAPTIVE) {
            return new AdaptiveLoadBalancer();
        }
        return null;
    }

    private static Serializer handleSerializer(int code) {
        if (code == SerializerCode.KRYO.getCode()) {
            return new KryoSerializer();
        }
        if (code == SerializerCode.JSON.getCode()) {
            return new JsonSerializer();
        }
        if (code == SerializerCode.HESSIAN.getCode()) {
            return new HessianSerializer();
        }
        return null;
    }

    private static Compresser handleCompress(int o) {
        if (o == CompressType.GZIP.getCode()) {
            return new GZipCompresser();
        }
        return null;
    }
}
