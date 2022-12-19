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

@Data
@Slf4j
public class ServerMonitor {
    private static Map<InetSocketAddress, ServerMonitorContent> map = new ConcurrentHashMap<>();

    public synchronized void updateStatus(ServerStatus serverStatus,InetSocketAddress address) {
        ServerMonitorContent content = map.get(address);
        content.setServerStatus(serverStatus);
        map.put(address, content);
    }

    public ServerMonitorContent getAllMonitorContent(InetSocketAddress address) {
        return map.get(address);
    }

    public void showAllMonitorContent() {
        for (InetSocketAddress key : map.keySet()) {
            ServerMonitorContent serverMonitorContent=map.get(key);
            serverMonitorContent.cal();
            log.info(key + ":" + map.get(key).toString());
        }
    }

    public synchronized void handleSend(InetSocketAddress address) {
        ServerMonitorContent serverMonitorContent = map.get(address);
        if (serverMonitorContent == null) {
            serverMonitorContent = new ServerMonitorContent(0, 0, ServerHealth.NOT_CONNECT,address,new ServerStatus());
        }
        serverMonitorContent.addSendCount();
        map.put(address, serverMonitorContent);
        if (DefaultConfig.SHOW_SERVER_STATUS_LOG) log.info("发送请求:\t地址:" + address.toString() + "\t状态:" + serverMonitorContent);
    }

    public synchronized void handleReceived(InetSocketAddress address) {
        ServerMonitorContent serverMonitorContent = map.get(address);
        serverMonitorContent.addReceivedCount();
        map.put(address, serverMonitorContent);
        if (DefaultConfig.SHOW_SERVER_STATUS_LOG) log.info("接收请求:\t地址:" + address.toString() + "\t状态:" + serverMonitorContent);
    }
}
