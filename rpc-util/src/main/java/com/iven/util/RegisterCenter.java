package com.iven.util;

import com.alibaba.fastjson.JSON;
import com.iven.util.entity.RpcRegisterEntity;
//import com.iven.util.entity.RpcRegisterEntity2;

public class RegisterCenter {
    private static final String ROOT = "/irpc";
    private static final String PROVIDER_PATH = "%s/%s/provider";
    private static final String CONSUMER_PATH = "%s/%s/consumer";

    public static void registerProvider(RpcRegisterEntity entity) {
        ZKUtil.writeData(String.format(PROVIDER_PATH, ROOT, entity.getInterfaceClassFullName()), JSON.toJSONString(entity));
    }

    public static void registerConsumer(RpcRegisterEntity entity) {
        ZKUtil.writeData(String.format(CONSUMER_PATH, ROOT, entity.getInterfaceClassFullName()), JSON.toJSONString(entity));
    }

    public static <T> T getProviderData(String path) {
        return ZKUtil.readData(String.format(PROVIDER_PATH, ROOT, path));
    }

    public static <T> T getConsumerData(String path) {
        return ZKUtil.readData(String.format(CONSUMER_PATH, ROOT, path));
    }
}
