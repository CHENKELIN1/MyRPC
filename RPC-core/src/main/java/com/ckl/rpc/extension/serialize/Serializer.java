package com.ckl.rpc.extension.serialize;

/**
 * 序列化接口
 */
public interface Serializer {
    /**
     * 序列化
     *
     * @param obj 序列化兑现
     * @return 序列化结果
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     *
     * @param bytes bytes
     * @param clazz 目标类
     * @return 反序列化结果
     */
    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();
}
