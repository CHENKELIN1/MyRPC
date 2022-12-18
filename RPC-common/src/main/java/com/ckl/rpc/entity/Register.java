package com.ckl.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Register{
    String serviceName;
    String group;
}
