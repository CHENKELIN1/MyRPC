package com.ckl.rpc.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

@Data
@Accessors(chain = true)
public class ServerStatus implements Serializable {
    private int receivedCount;
    private double cpuLoad;

    public ServerStatus() {
        OperatingSystemMXBean osmxb = ManagementFactory.getOperatingSystemMXBean();
        this.cpuLoad = osmxb.getSystemLoadAverage();
    }


    public void addReceived() {
        this.receivedCount++;
    }


    public void flushData() {
        OperatingSystemMXBean osmxb = ManagementFactory.getOperatingSystemMXBean();
        this.cpuLoad = osmxb.getSystemLoadAverage();
    }
}
