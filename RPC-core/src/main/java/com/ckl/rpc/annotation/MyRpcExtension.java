package com.ckl.rpc.annotation;

import com.ckl.rpc.enumeration.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyRpcExtension {
    SerializerCode serializerCode() default SerializerCode.NULL;
    LimiterType limitType() default LimiterType.NULL;
    LoadBalanceType loadBalanceType() default LoadBalanceType.NULL;
    CompressType compressType() default CompressType.NULL;
    RegistryCode registryCode() default RegistryCode.NULL;
}
