package provider.app;

import provider.service.Calculator;
import provider.service.CalculatorImpl;
import request.CalculateRpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ProviderApp {
    private final Calculator calculator = new CalculatorImpl();

    public static void main(String[] args) throws IOException {
        new ProviderApp().run();
    }

    public void run() throws IOException {
        //9090端口上监听
        ServerSocket listener = new ServerSocket(8080);
        try {
            Socket socket = listener.accept();
            while (true) {
                try {
                    //反序列化数据
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    Object object = objectInputStream.readObject();
                    int res = 0;
                    if (object instanceof CalculateRpcRequest) {
                        CalculateRpcRequest calculateRpcRequest = (CalculateRpcRequest) object;
                        if ("add".equals(calculateRpcRequest.getName())) {
                            res = calculator.add(calculateRpcRequest.getA(), calculateRpcRequest.getB());
                        } else {
                            throw new UnsupportedOperationException();
                        }
                    }
                    ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(new Integer(res));
                } catch (Exception e) {
                    System.out.println("反序列失败！");
                } finally {
                    socket.close();
                }
            }
        } finally {
            listener.close();
        }
    }
}
