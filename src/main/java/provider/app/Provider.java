package provider.app;

import request.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class Provider implements Runnable{
    private Socket client=null;
    private List<Object> serviceList=null;
    public Provider(Socket client,List<Object> services){
        this.client=client;
        this.serviceList=services;
    }
    public void run(){
        ObjectInputStream objectInputStream=null;
        ObjectOutputStream objectOutputStream=null;
        try{
            objectInputStream=new ObjectInputStream(client.getInputStream());
            objectOutputStream=new ObjectOutputStream(client.getOutputStream());
            RpcRequest rpcRequest=(RpcRequest)objectInputStream.readObject();
            // 读取类名
            Class serviceClass=(Class) rpcRequest.getServiceClass();
            Object obj=getService(serviceClass);
            if(obj==null){
                throw new Exception("not service");
            }
            // 读取方法名
            String methodName=rpcRequest.getMethodName();
            // 读取方法入参类型
            Class<?>[] parameterTypes=rpcRequest.getParameterTypes();
            // 读取方法调用入参
            Object[] parameters=rpcRequest.getArguments();
            System.out.println(String.format("收到消费者远程调用请求：类名 = {%s}，方法名 = {%s}，调用入参 = %s，方法入参列表 = %s",
                    serviceClass, methodName, Arrays.toString(parameters), Arrays.toString(parameterTypes)));
            Method method = obj.getClass().getMethod(methodName,parameterTypes);
            Object invoke = method.invoke(obj, parameters);
            System.out.println("方法调用结果：" + invoke);
            objectOutputStream.writeObject(invoke);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getService(Class servicesClass){
        for(Object obj:serviceList){
            boolean isFather=servicesClass.isAssignableFrom(obj.getClass());
            if(isFather){
                return obj;
            }
        }
        return null;
    }

//    public static void main(String[] args) throws IOException {
//        ServerSocket serverSocket = new ServerSocket(8889);
//        try{
//            Socket accept = serverSocket.accept();
//            ObjectInputStream objectInputStream = new ObjectInputStream(accept.getInputStream());
//            RpcRequest rpcRequest=(RpcRequest)objectInputStream.readObject();
//            // 读取类名
//            String classFullName=rpcRequest.getClassName();
//            // 读取方法名
//            String methodName=rpcRequest.getMethodName();
//            // 读取方法入参类型
//            Class<?>[] parameterTypes=rpcRequest.getParameterTypes();
//            // 读取方法调用入参
//            Object[] parameters=rpcRequest.getArguments();
//            System.out.println(String.format("收到消费者远程调用请求：类名 = {%s}，方法名 = {%s}，调用入参 = %s，方法入参列表 = %s",
//                    classFullName, methodName, Arrays.toString(parameters), Arrays.toString(parameterTypes)));
//            Class<?> aClass = Class.forName(classFullName);
//            Method method = aClass.getMethod(methodName, parameterTypes);
//            Object invoke = method.invoke(aClass.newInstance(), parameters);
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(accept.getOutputStream());
//            System.out.println("方法调用结果：" + invoke);
//            objectOutputStream.writeObject(invoke);
//        } catch (ClassNotFoundException | NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }
//    }
}
