package com.hidou7.toadmin.common.util;

import java.util.UUID;

public class UUIDUtil {
    
    public static String randomUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
