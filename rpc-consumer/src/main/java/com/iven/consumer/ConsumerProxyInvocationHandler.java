package com.iven.consumer;

import com.alibaba.fastjson.JSON;
import com.iven.util.RedisUtil;
import com.iven.util.RegisterCenter;
import com.iven.util.entity.RpcRegisterEntity;
//import com.iven.util.entity.RpcRegisterEntity2;
import com.iven.util.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


public class ConsumerProxyInvocationHandler implements InvocationHandler {
    private final Logger logger = LoggerFactory.getLogger(ConsumerProxyInvocationHandler.class);
    private final String PROVIDER_KEY = "%s:provider";
    /**
     * 代理类的class
     */
    private Class<?> serviceClass;

    public ConsumerProxyInvocationHandler(Class<?> serviceClass) {
        this.serviceClass = serviceClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("proxy: {}", proxy.getClass().getName());
        logger.info("method: {}", method);

        String interfaceName = serviceClass.getName();
        String serviceObject= RegisterCenter.getProviderData(interfaceName);
        RpcRegisterEntity rpcRegisterEntity = JSON.parseObject(serviceObject, RpcRegisterEntity.class);
        rpcRegisterEntity.setParameters(args);
        RpcClient rpcClient = getClient(rpcRegisterEntity.getHost(), rpcRegisterEntity.getPort());
        try{
            Object responseMsg = rpcClient.send(JSON.toJSONString(rpcRegisterEntity));
            RpcResponse rpcResponse = JSON.parseObject(responseMsg.toString(), RpcResponse.class);
            if (rpcResponse.getException() != null) {
                throw new RuntimeException(rpcResponse.getException());
            }
            logger.info("执行方法完成，这里可以实现aop的after需求，消费者远程调用返回结果：{}", rpcResponse.getResult());
            return rpcResponse.getResult();
        } catch (ExecutionException | InterruptedException | RuntimeException e) {
            e.printStackTrace();
            return e.getMessage();
        }
//
//        String interfaceName = serviceClass.getName();
////        String serviceObject = RedisUtil.getObject(String.format(PROVIDER_KEY, interfaceName));
//        String serviceObject= RegisterCenter.getProviderData(interfaceName);
//        RpcRegisterEntity rpcRegisterEntity = JSON.parseObject(serviceObject, RpcRegisterEntity.class);
//        logger.info("args: {}", Arrays.toString(args));
//        Socket socket = new Socket("127.0.0.1", rpcRegisterEntity.getPort());
////        Socket socket=new Socket(rpcRegisterEntity.getHost(), rpcRegisterEntity.getPort());
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//        // 写接口类名
//        objectOutputStream.writeUTF(interfaceName);
//        // 发送方法名
//        objectOutputStream.writeUTF(method.getName());
//        // 写方法参数列表
//        objectOutputStream.writeObject(method.getParameterTypes());
//        // 写调用入参参数
//        objectOutputStream.writeObject(args);
//        // 读取返回值
//        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//        logger.info("执行方法前，这里可以实现aop的before需求");
//        Object o = objectInputStream.readObject();
//        logger.info("执行方法完成，这里可以实现aop的after需求，消费者远程调用返回结果：{}", o);
//        return "hello, proxyInvoke, result = " + o;
    }
    private RpcClient getClient(String ip,int host) {
        RpcClient rpcClient = new RpcClient(ip, host);
        return rpcClient;
    }
}
