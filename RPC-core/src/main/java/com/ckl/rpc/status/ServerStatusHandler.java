package com.ckl.rpc.status;

import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.entity.ServerStatus;
import com.ckl.rpc.factory.SingletonFactory;

import java.net.InetSocketAddress;

public class ServerStatusHandler {
    public static ServerStatus updateStatus(ServerStatus serverStatus){
        serverStatus.flushData();
        return serverStatus;
    }

    public static void handleReceived(RpcResponse response,InetSocketAddress address){
        ServerStatus status = response.getStatus();
        ServerMonitor serverMonitor = SingletonFactory.getInstance(ServerMonitor.class);
        serverMonitor.updateStatus(status,address);
        serverMonitor.handleReceived(address);
    }
    public static void handleSend(InetSocketAddress address){
        ServerMonitor serverMonitor = SingletonFactory.getInstance(ServerMonitor.class);
        serverMonitor.handleSend(address);
    }
}
