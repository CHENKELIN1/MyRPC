package com.ckl.rpc.factory;


import com.ckl.rpc.annotation.MyRpcExtension;
import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.enumeration.*;
import com.ckl.rpc.exception.RpcException;
import com.ckl.rpc.extension.compress.Compresser;
import com.ckl.rpc.extension.limit.Limiter;
import com.ckl.rpc.extension.loadbalance.LoadBalancer;
import com.ckl.rpc.extension.serialize.Serializer;
import com.ckl.rpc.extension.registry.RegistryHandler;
import com.ckl.rpc.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class ExtensionFactory {
    private static final Map<Integer, Map<Integer, Class<?>>> total = new HashMap<>();
    private static final int SERIALIZER = 1;
    private static final int LOAD_BALANCE = 2;
    private static final int COMPRESS = 3;
    private static final int LIMIT = 4;
    private static final int REGISTRY = 5;

    static {
        String defaultPackage = DefaultConfig.DEFAULT_EXTENSION_PACKAGE;
        total.put(SERIALIZER, new HashMap<>());
        total.put(LOAD_BALANCE, new HashMap<>());
        total.put(COMPRESS, new HashMap<>());
        total.put(LIMIT, new HashMap<>());
        total.put(REGISTRY,new HashMap<>());
        Set<Class<?>> classes = ReflectUtil.getClasses(defaultPackage);
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(MyRpcExtension.class)) {
                MyRpcExtension annotation = clazz.getAnnotation(MyRpcExtension.class);
                if (annotation.serializerCode() != SerializerCode.NULL) {
                    int code = annotation.serializerCode().getCode();
                    total.get(SERIALIZER).put(code, clazz);
                } else if (annotation.compressType() != CompressType.NULL) {
                    int code = annotation.compressType().getCode();
                    total.get(COMPRESS).put(code, clazz);
                } else if (annotation.limitType() != LimiterType.NULL) {
                    int code = annotation.limitType().getCode();
                    total.get(LIMIT).put(code, clazz);
                } else if (annotation.loadBalanceType() != LoadBalanceType.NULL) {
                    int code = annotation.loadBalanceType().getCode();
                    total.get(LOAD_BALANCE).put(code, clazz);
                } else if (annotation.registryCode() != RegistryCode.NULL) {
                    int code = annotation.registryCode().getCode();
                    total.get(REGISTRY).put(code,clazz);
                } else {
                    log.error("该扩展类注解:@MyRpcExtension 未标明类别 {}", clazz.getName());
                }
            }
        }
    }

    public static <T> T getExtension(Class<T> interfaceName, int code) {
        String name = interfaceName.getName();
        Class<?> clazz = null;
        if (name.equals(Serializer.class.getName())) {
            clazz = total.get(SERIALIZER).get(code);
        } else if (name.equals(LoadBalancer.class.getName())) {
            clazz = total.get(LOAD_BALANCE).get(code);
        } else if (name.equals(Limiter.class.getName())) {
            clazz = total.get(LIMIT).get(code);
        } else if (name.equals(Compresser.class.getName())) {
            clazz = total.get(COMPRESS).get(code);
        } else if (name.equals(RegistryHandler.class.getName())) {
            clazz = total.get(REGISTRY).get(code);
        }
        if (clazz == null) {
            throw new RpcException(RpcError.CAN_NOT_FIND_EXTENSION);
        } else {
            try {
                return (T) clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                log.error("查询不到该扩展功能,该扩展类未添加注解:@MyRpcExtension,interfaceName:{},code:{}",interfaceName,code);
                throw new RpcException(RpcError.CAN_NOT_FIND_EXTENSION);
            }
        }
    }
}
