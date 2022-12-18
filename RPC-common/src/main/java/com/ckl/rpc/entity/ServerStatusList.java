package com.ckl.rpc.entity;

import lombok.Data;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class ServerStatusList {
    private static Map<InetSocketAddress,ServerStatus> map=new ConcurrentHashMap<>();

    public void addData(ServerStatus serverStatus){
        InetSocketAddress address=new InetSocketAddress(serverStatus.getHost(),serverStatus.getPort());
        map.put(address,serverStatus);
    }
    public ServerStatus getList(InetSocketAddress address){
        return map.get(address);
    }
    public void showData(){
        for (InetSocketAddress key: map.keySet()){
            System.out.println(key+":"+map.get(key).toString());
        }
    }
}
