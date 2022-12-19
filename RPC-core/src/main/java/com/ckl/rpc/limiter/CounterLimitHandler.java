package com.ckl.rpc.limiter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounterLimitHandler implements LimitHandler{
    int count;
    @Override
    public synchronized void preHandle() {
        count--;
    }

    @Override
    public boolean limit() {
        if (count<0) return false;
        return true;
    }

    @Override
    public synchronized void afterHandle() {
        count++;
    }

}
