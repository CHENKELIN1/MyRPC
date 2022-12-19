package com.ckl.rpc.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

/**
 * 服务器状态
 */
@Data
@Accessors(chain = true)
public class ServerStatus implements Serializable {
    //    接收到请求数量
    private int receivedCount;
    //    cpu使用率
    private double cpuLoad;

    public ServerStatus() {
        OperatingSystemMXBean osmxb = ManagementFactory.getOperatingSystemMXBean();
        this.cpuLoad = osmxb.getSystemLoadAverage();
    }

    /**
     * 增加receivedCount
     */
    public void addReceived() {
        this.receivedCount++;
    }

    /**
     * 更新服务器状态
     */
    public void flushData() {
        OperatingSystemMXBean osmxb = ManagementFactory.getOperatingSystemMXBean();
        this.cpuLoad = osmxb.getSystemLoadAverage();
    }
}
