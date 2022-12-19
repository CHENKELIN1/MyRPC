package com.ckl.rpc.status;

import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.entity.ServerStatus;
import com.ckl.rpc.factory.SingletonFactory;

import java.net.InetSocketAddress;

/**
 * 服务监控处理器
 */
public class ServerStatusHandler {
    /**
     * 更新状态
     *
     * @param serverStatus
     * @return
     */
    public static ServerStatus updateStatus(ServerStatus serverStatus) {
        serverStatus.flushData();
        return serverStatus;
    }

    /**
     * 处理received
     *
     * @param response
     * @param address
     */
    public static void handleReceived(RpcResponse response, InetSocketAddress address) {
        ServerStatus status = response.getStatus();
        ServerMonitor serverMonitor = SingletonFactory.getInstance(ServerMonitor.class);
        serverMonitor.updateStatus(status, address);
        serverMonitor.handleReceived(address);
    }

    /**
     * 处理send
     *
     * @param address
     */
    public static void handleSend(InetSocketAddress address) {
        ServerMonitor serverMonitor = SingletonFactory.getInstance(ServerMonitor.class);
        serverMonitor.handleSend(address);
    }
}
