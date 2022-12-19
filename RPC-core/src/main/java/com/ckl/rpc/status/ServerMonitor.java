package com.ckl.rpc.status;

import com.ckl.rpc.config.DefaultConfig;
import com.ckl.rpc.entity.ServerMonitorContent;
import com.ckl.rpc.entity.ServerStatus;
import com.ckl.rpc.enumeration.ServerHealth;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端的服务器监控器
 */
@Data
@Slf4j
public class ServerMonitor {
    //    存储服务器与服务器监控内容
    private static Map<InetSocketAddress, ServerMonitorContent> map = new ConcurrentHashMap<>();

    /**
     * 更新状态
     *
     * @param serverStatus 服务器状态
     * @param address      服务器地址
     */
    public synchronized void updateStatus(ServerStatus serverStatus, InetSocketAddress address) {
        ServerMonitorContent content = map.get(address);
        content.setServerStatus(serverStatus);
        map.put(address, content);
    }

    /**
     * 获取监控内容
     *
     * @param address
     * @return
     */
    public ServerMonitorContent getMonitorContent(InetSocketAddress address) {
        return map.get(address);
    }

    /**
     * 输出所有服务器状态
     */
    public void showAllMonitorContent() {
        for (InetSocketAddress key : map.keySet()) {
            ServerMonitorContent serverMonitorContent = map.get(key);
            serverMonitorContent.cal();
            log.info(key + ":" + map.get(key).toString());
        }
    }

    /**
     * 处理send过程
     *
     * @param address 服务器地址
     */
    public synchronized void handleSend(InetSocketAddress address) {
//        获取监控内容
        ServerMonitorContent serverMonitorContent = map.get(address);
//        为空则创建
        if (serverMonitorContent == null) {
            serverMonitorContent = new ServerMonitorContent(0, 0, ServerHealth.NOT_CONNECT, address, new ServerStatus());
        }
//        send计数
        serverMonitorContent.addSendCount();
        map.put(address, serverMonitorContent);
        if (DefaultConfig.SHOW_SERVER_STATUS_LOG)
            log.info("发送请求:\t地址:" + address.toString() + "\t状态:" + serverMonitorContent);
    }

    /**
     * 处理received过程
     *
     * @param address
     */
    public synchronized void handleReceived(InetSocketAddress address) {
//        获取监控内容
        ServerMonitorContent serverMonitorContent = map.get(address);
//        received计数
        serverMonitorContent.addReceivedCount();
        map.put(address, serverMonitorContent);
        if (DefaultConfig.SHOW_SERVER_STATUS_LOG)
            log.info("接收请求:\t地址:" + address.toString() + "\t状态:" + serverMonitorContent);
    }
}
