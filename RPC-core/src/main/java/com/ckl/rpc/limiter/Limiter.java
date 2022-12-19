package com.ckl.rpc.limiter;

import com.ckl.rpc.enumeration.LimiterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Limiter implements LimitHandler {
    LimitHandler limitHandler;

    @Override
    public  void preHandle() {
        limitHandler.preHandle();
    }

    @Override
    public boolean limit() {
        log.info("限流器:limit"+limitHandler.toString());
        return limitHandler.limit();
    }

    @Override
    public  void afterHandle() {
        limitHandler.afterHandle();
    }

}