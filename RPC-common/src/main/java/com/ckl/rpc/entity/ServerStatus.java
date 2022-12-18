package com.ckl.rpc.entity;

import lombok.Data;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

@Data
public class ServerStatus implements Serializable {
    private int receivedCount;
    private int failCount;
    private double cpuLoad;
    private String host;
    private int port;

    public ServerStatus(){

    }
    public ServerStatus(String host,int port) {
        OperatingSystemMXBean osmxb = ManagementFactory.getOperatingSystemMXBean();
        this.cpuLoad = osmxb.getSystemLoadAverage();
        this.host=host;
        this.port=port;
    }


    public void addReceived() {
        this.receivedCount++;
    }

    public void addFail() {
        this.failCount++;
    }

    public void flushData() {
        OperatingSystemMXBean osmxb = ManagementFactory.getOperatingSystemMXBean();
        this.cpuLoad = osmxb.getSystemLoadAverage();
    }
}
