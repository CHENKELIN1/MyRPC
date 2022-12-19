package com.ckl.rpc.entity;

import com.ckl.rpc.enumeration.ServerHealth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetSocketAddress;

/**
 * 服务监控内容
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerMonitorContent {
    //    已发送包数量
    int sendCount;
    //    已收到包数量
    int receivedCount;
    //    服务器健康状态
    ServerHealth serverHealth;
    //    服务器地址
    InetSocketAddress address;
    //    服务器状态
    ServerStatus serverStatus;

    /**
     * 增加sendCount
     */
    public synchronized void addSendCount() {
        this.sendCount++;
    }

    /**
     * 增加ReceivedCount
     */
    public synchronized void addReceivedCount() {
        this.receivedCount++;
    }

    /**
     * 计算服务器状态
     */
    public void cal() {
        if (sendCount == 0) {
            serverHealth = ServerHealth.NOT_CONNECT;
            return;
        }
        double score = receivedCount / (sendCount * 1.0);
        if (score > 0.9) serverHealth = ServerHealth.HEALTH;
        else if (score > 0.5) serverHealth = ServerHealth.SUB_HEALTH;
        else serverHealth = ServerHealth.DEATH;
    }
}
