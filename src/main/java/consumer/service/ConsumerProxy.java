package consumer.service;

import request.RpcRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

    public class ConsumerProxy implements InvocationHandler {

        private String ip;
        private int port;

        public ConsumerProxy(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
            Socket socket = new Socket(ip,port);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            RpcRequest rpcRequest = new RpcRequest(proxy.getClass().getInterfaces()[0], method.getName(), method.getParameterTypes(), args);
            objectOutputStream.writeObject(rpcRequest);
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            return inputStream.readObject();
        }
    }
