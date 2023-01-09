package com.ckl.rpc.status;

import com.ckl.rpc.entity.RpcResponse;
import com.ckl.rpc.entity.Status;
import com.ckl.rpc.factory.SingletonFactory;

import java.net.InetSocketAddress;

/**
 * 服务监控处理器
 */
public class StatusHandler {
    /**
     * 更新状态
     *
     * @param status
     * @return
     */
    public static Status ServerUpdateStatus(Status status) {
        status.flushData();
        return status;
    }

    /**
     * 处理received
     *
     * @param response
     * @param address
     */
    public static void ClientHandleReceived(RpcResponse response, InetSocketAddress address) {
        Status status = response.getStatus();
        ClientMonitor clientMonitor = SingletonFactory.getInstance(ClientMonitor.class);
        clientMonitor.updateStatus(status, address);
        clientMonitor.handleReceived(address);
    }

    /**
     * 处理send
     *
     * @param address
     */
    public static void ServerHandleSend(InetSocketAddress address) {
        ClientMonitor clientMonitor = SingletonFactory.getInstance(ClientMonitor.class);
        clientMonitor.handleSend(address);
    }
}
