package com.iven.util;

import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.I0Itec.zkclient.ZkClient;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZKUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZKUtil.class);
    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    private static ZkClient client;

    static {
        client = new ZkClient(ZK_ADDRESS);
    }

    public static void writeData(String path, String data) {
        if (!client.exists(path)) {
            // 创建持久化节点 ,初始化数据
            client.createPersistent(path, true);
        }
        // 修改节点数据,并返回该节点的状态
        client.writeData(path, data, -1);
    }

    public static <T> T readData(String path) {
        // 获取节点数据
        return client.readData(path);
    }
}
