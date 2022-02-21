package com.iven.proccess;

import com.google.common.collect.Maps;

import java.util.Map;

public class RpcClientContentHandler {
    private static Map<Class, Object> rpcClientContentMap = Maps.newHashMap();

    public static void initRpcClientContent(Class zlass, Object newInstance) {
        if (rpcClientContentMap.containsKey(zlass)) {
            return;
        }
        rpcClientContentMap.put(zlass, newInstance);
    }

    public static Map<Class, Object> getRpcClientContentMap() {
        return rpcClientContentMap;
    }
}
