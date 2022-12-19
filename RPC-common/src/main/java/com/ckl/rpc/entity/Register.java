package com.ckl.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 服务注册对象
 */
@Data
@AllArgsConstructor
public class Register {
    //    服务名称
    String serviceName;
    //    所属组
    String group;
}
