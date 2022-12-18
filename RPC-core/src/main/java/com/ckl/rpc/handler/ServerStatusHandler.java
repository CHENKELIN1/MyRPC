package com.ckl.rpc.handler;

import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.entity.ServerStatus;
import com.ckl.rpc.entity.ServerStatusList;
import com.ckl.rpc.factory.SingletonFactory;

public class ServerStatusHandler {
    public static ServerStatus handle(ServerStatus serverStatus){
        serverStatus.flushData();
        return serverStatus;
    }
    public static void save(RpcResponse response){
        ServerStatus status = response.getStatus();
        ServerStatusList statusList = SingletonFactory.getInstance(ServerStatusList.class);
        statusList.addData(status);
    }
}
