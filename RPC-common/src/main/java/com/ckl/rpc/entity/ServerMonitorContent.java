package com.ckl.rpc.entity;

import com.ckl.rpc.enumeration.ServerHealth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetSocketAddress;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerMonitorContent {
    int sendCount;
    int receivedCount;
    ServerHealth serverHealth;

    InetSocketAddress address;
    ServerStatus serverStatus;

    public synchronized void addSendCount() {
        this.sendCount++;
    }

    public synchronized void addReceivedCount() {
        this.receivedCount++;
    }

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
