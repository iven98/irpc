package consumer.service;

import provider.service.Calculator;
import request.CalculateRpcRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CalculatorRemoteImpl implements Calculator {

    @Override
    public int add(int a, int b) {
        //返回远程调用的地址
        List<String> addressList=lookupProviders();
        String address=chooseAddress(addressList);
        try{
            //和远程服务建立socket连接
            Socket socket=new Socket(address,PORT);
            //参数序列化
            CalculateRpcRequest calculateRpcRequest=generateRequest(a,b);
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream());

            //请求发给提供方
            objectOutputStream.writeObject(calculateRpcRequest);

            ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream());
            Object response =objectInputStream.readObject();
            System.out.println(response);
            if(response instanceof Integer){
                return (Integer) response;
            }
            else{
                throw new InternalError();
            }
        } catch (Exception e) {
            System.out.println("ERROR!");
            throw new InternalError();
        }
    }

    @Override
    public int sub(int a, int b) {
        return 0;
    }

    @Override
    public int mul(int a, int b) {
        return 0;
    }

    @Override
    public int div(int a, int b) {
        return 0;
    }

    public CalculateRpcRequest generateRequest(int a,int b){
        CalculateRpcRequest calculateRpcRequest=new CalculateRpcRequest();
        calculateRpcRequest.setA(a);
        calculateRpcRequest.setB(b);
        calculateRpcRequest.setName("add");
        return calculateRpcRequest;
    }

    public static List<String> lookupProviders(){
        List<String> address=new ArrayList<>();
        address.add("127.0.0.1");
        return address;
    }

    private String chooseAddress(List<String> address){
        if(null==address||address.size()==0){
            throw new IllegalArgumentException();
        }
        return address.get(0);
    }

    public static final int PORT = 8080;
}
