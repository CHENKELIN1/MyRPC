package com.ckl.rpc.config;

import com.ckl.rpc.enumeration.GroupName;
import com.ckl.rpc.enumeration.LoadBalanceType;
import com.ckl.rpc.enumeration.SerializerCode;
import com.ckl.rpc.enumeration.TransmissionType;

public interface DefaultConfig {
    SerializerCode DEFAULT_SERIALIZER = SerializerCode.HESSIAN;
    TransmissionType DEFAULT_TRANSMISSION= TransmissionType.NETTY;
    LoadBalanceType DEFAULT_LOAD_BALANCE = LoadBalanceType.LOAD_BALANCE_RANDOM;
    String DEFAULT_NACOS_SERVER_ADDRESS= "81.68.85.4:8850";
    String DEFAULT_PACKAGE = "com.ckl.rpc";
    String DEFAULT_GROUP = GroupName.GROUP_0;
}
