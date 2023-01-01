package com.ckl.rpc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {
    public static String formatDate(Date date) {
        return new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss").format(date);
    }
}
