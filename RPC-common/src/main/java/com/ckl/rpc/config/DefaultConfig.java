package com.ckl.rpc.config;

import com.ckl.rpc.enumeration.*;

import java.util.concurrent.TimeUnit;

/**
 * 全局配置内容
 */
public interface DefaultConfig {
    /*
     * client server config
     * */
    SerializerCode DEFAULT_SERIALIZER = SerializerCode.PROTOBUF;
    TransmissionType DEFAULT_TRANSMISSION = TransmissionType.NETTY;
    LoadBalanceType DEFAULT_LOAD_BALANCE = LoadBalanceType.LOAD_BALANCE_CONSISTENT_HASH;
    LimiterType DEFAULT_LIMITER = LimiterType.TOKEN_BUCKET;
    CompressType DEFAULT_COMPRESSER = CompressType.GZIP;
    RegistryCode DEFAULT_SERVER_REGISTRY = RegistryCode.REDIS;
    /*
     * nacos config
     * */
    String DEFAULT_NACOS_SERVER_ADDRESS = "81.68.85.4:8850";
    String DEFAULT_REDIS_HOST = "localhost";
    Integer DEFAULT_REDIS_PORT = 6379;
    /*
     * server scan config
     * */
    String DEFAULT_PACKAGE = "com.ckl.rpc";
    String DEFAULT_EXTENSION_PACKAGE = "com.ckl.rpc.extension";
    /*
     * service group config
     * */
    String DEFAULT_GROUP = GroupName.GROUP_0;
    /*
     * netty config
     * */
    long NETTY_WRITER_IDLE_TIME = 5;
    long NETTY_READER_IDLE_TIME = 5;
    long NETTY_ALL_IDLE_TIME = 5;
    TimeUnit NETTY_IDLE_TIME_UNIT = TimeUnit.SECONDS;
    /*
     * limiter config
     * */
    int LIMIT_COUNTER_LIMITER_COUNT = 5;
    int LIMIT_FUNNEL_CAPACITY = 10;
    double LIMIT_FUNNEL_LEAKING_RATE = 5;
    int LIMIT_TOKEN_BUCKET_CAPACITY = 10;
    int LIMIT_TOKEN_BUCKET_RATE = 5;
    /*
    自定义协议
     */
    int EXPEND_LENGTH = 19 + 4;
}
