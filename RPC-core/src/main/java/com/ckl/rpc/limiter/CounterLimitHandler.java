package com.ckl.rpc.limiter;

import lombok.Data;

@Data
public class CounterLimitHandler implements LimitHandler{
    int count;
    @Override
    public synchronized void preHandle() {
        count++;
    }

    @Override
    public boolean limit() {
        if (count>5) return false;
        return true;
    }

    @Override
    public synchronized void afterHandle() {
        count--;
    }

}
