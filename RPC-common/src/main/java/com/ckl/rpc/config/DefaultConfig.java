package com.ckl.rpc.config;

import com.ckl.rpc.enumeration.GroupName;
import com.ckl.rpc.enumeration.LoadBalanceType;
import com.ckl.rpc.enumeration.SerializerCode;
import com.ckl.rpc.enumeration.TransmissionType;

import java.util.concurrent.TimeUnit;

/**
 * 全局配置内容
 */
public interface DefaultConfig {
    /*
     * client server config
     * */
    SerializerCode DEFAULT_SERIALIZER = SerializerCode.HESSIAN;
    TransmissionType DEFAULT_TRANSMISSION = TransmissionType.NETTY;
    LoadBalanceType DEFAULT_LOAD_BALANCE = LoadBalanceType.LOAD_BALANCE_RANDOM;
    /*
     * nacos config
     * */
    String DEFAULT_NACOS_SERVER_ADDRESS = "81.68.85.4:8850";
    /*
     * server scan config
     * */
    String DEFAULT_PACKAGE = "com.ckl.rpc";
    /*
     * service group config
     * */
    String DEFAULT_GROUP = GroupName.GROUP_0;
    /*
     * netty config
     * */
    long NETTY_WRITER_IDLE_TIME = 60;
    long NETTY_READER_IDLE_TIME = 60;
    long NETTY_ALL_IDLE_TIME = 60;
    TimeUnit NETTY_IDLE_TIME_UNIT = TimeUnit.SECONDS;
    /*
     * log config
     * */
    boolean SERVER_SHOW_DETAIL_REQUEST_LOG = false;
    boolean SERVER_SHOW_DETAIL_RESPONSE_LOG = false;
    boolean SERVER_SHOW_HEART_BEAT_LOG = false;
    boolean CLIENT_SHOW_DETAIL_REQUEST_LOG = false;
    boolean CLIENT_SHOW_DETAIL_RESPONSE_LOG = false;
    boolean CLIENT_SHOW_HEART_BEAT_LOG = false;
    boolean SHOW_SERVER_STATUS_LOG = false;
    boolean LIMIT_LOG = true;
    /*
     * limiter config
     * */
    int SERVER_LIMIT_COUNT = 5;
    int CLIENT_LIMIT_COUNT = 5;
}
