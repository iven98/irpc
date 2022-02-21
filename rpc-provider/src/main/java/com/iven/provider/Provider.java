package com.iven.provider;

import com.alibaba.fastjson.JSON;
import com.iven.annotation.RpcComponentScan;
import com.iven.annotation.RpcProvider;
import com.iven.proccess.ClassScanner;
import com.iven.util.RegisterCenter;
import com.iven.util.entity.RpcRegisterEntity;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Set;


//@RpcProvider
@RpcComponentScan("com.iven.service")
public class Provider {
    static final String PROVIDER_KEY = "%s:provider";
    static final int port=8889;
    public static void main(String[] args) {
        try {
            ClassScanner.init(Provider.class);
            initServiceProvider();
            //启动nettyServer
            NettyServer nettyServer=new NettyServer("192.168.31.214",port);
            nettyServer.startNettyServer();
//
//            ServerSocket serverSocket = new ServerSocket(port);

//
//            while (true) {
//                Socket accept = serverSocket.accept();
//                ObjectInputStream objectInputStream = new ObjectInputStream(accept.getInputStream());
//                // 读取类名
//                String interfaceName = objectInputStream.readUTF();
//                // 读取方法名
//                String methodName = objectInputStream.readUTF();
//                // 读取方法入参类型
//                Class<?>[] parameterTypes = (Class<?>[])objectInputStream.readObject();
//                // 读取方法调用入参
//                Object[] parameters = (Object[])objectInputStream.readObject();
//                System.out.println(String.format("收到消费者远程调用请求：类名 = {%s}，方法名 = {%s}，调用入参 = %s，方法入参列表 = %s",
//                        interfaceName, methodName, Arrays.toString(parameters), Arrays.toString(parameterTypes)));
//
////                String serviceObject = RedisUtil.getObject(String.format(PROVIDER_KEY, classFullName));
//                String serviceObject=RegisterCenter.getProviderData(interfaceName);
//                RpcRegisterEntity rpcRegisterEntity = JSON.parseObject(serviceObject, RpcRegisterEntity.class);
//                Class<?> aClass = Class.forName(rpcRegisterEntity.getServiceImplClassFullName());
//                Method method = aClass.getMethod(methodName, parameterTypes);
//                Object invoke = method.invoke(aClass.newInstance(), parameters);
//                // 回写返回值
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(accept.getOutputStream());
//                System.out.println("方法调用结果：" + invoke);
//                objectOutputStream.writeObject(invoke);
//            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initServiceProvider() {
        Set<Class> classSet = ClassScanner.getClassSet();
        classSet.forEach(c -> {
            Annotation annotation = c.getAnnotation(RpcProvider.class);
            if (Objects.nonNull(annotation)) {
                Method[] methods = c.getDeclaredMethods();
                for (Method method : methods) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    String methodName = method.getName();
                    try {
                        Class[] interfaces = c.getInterfaces();
                        String interfaceName = interfaces[0].getName();
//                        RedisUtil.record2Cache(String.format(PROVIDER_KEY, interfaceName), JSON.toJSONString(rpcRegisterEntity));
                        RpcRegisterEntity rpcRegisterEntity=new RpcRegisterEntity(interfaceName, InetAddress.getLocalHost().getHostAddress(),port);
                        rpcRegisterEntity.setServiceImplClassFullName(c.getName());
                        rpcRegisterEntity.setMethodName(methodName);
                        rpcRegisterEntity.setParameterTypes(parameterTypes);
                        RegisterCenter.registerProvider(rpcRegisterEntity);
                        System.out.println(JSON.toJSONString(rpcRegisterEntity));
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
