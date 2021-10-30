package consumer.service;

import request.RpcRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

public class ConsumerProxy implements InvocationHandler {

    private final Class<?> serviceClass;

    public ConsumerProxy(Class<?> serviceClass) {
        this.serviceClass = serviceClass;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Socket socket = new Socket("127.0.0.1", 8889);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        RpcRequest rpcRequest = new RpcRequest(serviceClass.getName(), method.getName(), method.getParameterTypes(), args);
        objectOutputStream.writeObject(rpcRequest);
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        return inputStream.readObject();
    }
}
