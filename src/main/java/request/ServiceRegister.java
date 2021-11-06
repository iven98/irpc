package request;

import provider.app.Provider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ServiceRegister {
    private static List<Object> serviceList;

    //发布一个rpc服务
    public static void export(int port,Object... service) throws IOException {
        serviceList= Arrays.asList(service);
        ServerSocket server = new ServerSocket(port);
        Socket client=null;
        while (true){
            client=server.accept();
            new Thread(new Provider(client,serviceList)).start();
        }
    }
}
