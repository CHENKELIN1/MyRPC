package com.ckl.rpc;

import com.ckl.rpc.factory.ExtensionFactory;
import com.ckl.rpc.enumeration.SerializerCode;
import com.ckl.rpc.extension.serialize.Serializer;

import java.lang.reflect.InvocationTargetException;

public class test {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Serializer extension = ExtensionFactory.getExtension(Serializer.class, SerializerCode.KRYO.getCode());
        System.out.println("extension = " + extension);

    }

}
