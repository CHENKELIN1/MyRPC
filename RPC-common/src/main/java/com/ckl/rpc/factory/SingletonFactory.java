package com.ckl.rpc.factory;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例工厂
 */
@Slf4j
public class SingletonFactory {

    private static final Map<Class<?>,Object> objectMap = new ConcurrentHashMap<>();
    private SingletonFactory() {
    }

    /**
     * 根据类获取单例对象
     *
     * @param clazz 要获取的类
     * @return <T> 返回单例对象
     */
    public static <T> T getInstance(Class<T> clazz) {
        Object instance = objectMap.get(clazz);
        synchronized (clazz) {
            if (instance == null) {
                try {
                    log.info("创建单例:" + clazz.getCanonicalName());
                    instance = clazz.getDeclaredConstructor().newInstance();
                    objectMap.put(clazz, instance);
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
        return clazz.cast(instance);
    }
}
