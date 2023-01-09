package com.ckl.rpc.status;

import com.ckl.rpc.entity.ClientMonitorContent;
import com.ckl.rpc.entity.Status;
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
public class ClientMonitor {
    //    存储服务器与服务器监控内容
    private static Map<InetSocketAddress, ClientMonitorContent> map = new ConcurrentHashMap<>();

    /**
     * 更新状态
     *
     * @param status  服务器状态
     * @param address 服务器地址
     */
    public synchronized void updateStatus(Status status, InetSocketAddress address) {
        ClientMonitorContent content = map.get(address);
        content.setStatus(status);
        map.put(address, content);
    }

    /**
     * 获取监控内容
     *
     * @param address
     * @return
     */
    public synchronized ClientMonitorContent getMonitorContent(InetSocketAddress address) {
        return map.get(address);
    }

    /**
     * 输出所有服务器状态
     */
    public synchronized void showAllMonitorContent() {
        for (InetSocketAddress key : map.keySet()) {
            ClientMonitorContent clientMonitorContent = map.get(key);
            clientMonitorContent.cal();
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
        ClientMonitorContent clientMonitorContent = map.get(address);
//        为空则创建
        if (clientMonitorContent == null) {
            clientMonitorContent = new ClientMonitorContent(0, 0, ServerHealth.NOT_CONNECT, address, new Status());
        }
//        send计数
        clientMonitorContent.addSendCount();
        map.put(address, clientMonitorContent);
        log.debug("发送请求:\t地址:" + address.toString() + "\t状态:" + clientMonitorContent);
    }

    /**
     * 处理received过程
     *
     * @param address
     */
    public synchronized void handleReceived(InetSocketAddress address) {
//        获取监控内容
        ClientMonitorContent clientMonitorContent = map.get(address);
//        received计数
        clientMonitorContent.addReceivedCount();
        map.put(address, clientMonitorContent);
        log.debug("接收请求:\t地址:" + address.toString() + "\t状态:" + clientMonitorContent);
    }
}
