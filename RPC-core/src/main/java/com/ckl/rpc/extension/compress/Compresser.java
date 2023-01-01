package com.ckl.rpc.extension.compress;

public interface Compresser {
    byte[] compress(byte[] bytes);


    byte[] decompress(byte[] bytes);
}
