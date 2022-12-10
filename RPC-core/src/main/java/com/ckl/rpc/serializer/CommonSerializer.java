package com.ckl.rpc.serializer;

/**
 * 序列化接口
 */
public interface CommonSerializer {
    Integer KRYO_SERIALIZER = 0;
    Integer JSON_SERIALIZER = 1;
    Integer HESSIAN_SERIALIZER = 2;
    Integer PROTOBUF_SERIALIZER = 3;
    Integer DEFAULT_SERIALIZER = KRYO_SERIALIZER;

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

    /**
     * 通过序列化方式获取序列化对象
     *
     * @param code
     * @return
     */
    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            case 2:
                return new HessianSerializer();
            default:
                return null;
        }
    }
}
