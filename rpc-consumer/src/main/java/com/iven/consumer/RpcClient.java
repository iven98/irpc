package com.iven.consumer;

import com.iven.annotation.RpcConsumer;
import com.iven.netty.JSONSerializer;
import com.iven.netty.RpcDecoder;
import com.iven.netty.RpcEncoder;
import com.iven.proccess.ClassScanner;
import com.iven.util.entity.RpcRegisterEntity;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.*;


public class RpcClient{
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);
    private EventLoopGroup group;

    private Channel channel;

    private String ip;

    private int port;

    private RpcConsumerHandler rpcConsumerHandler=new RpcConsumerHandler();

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public RpcClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        initClient();
    }

    public void initClient() {
        try {
            //1.创建线程组
            group = new NioEventLoopGroup();
            //2.创建启动助手
            Bootstrap bootstrap = new Bootstrap();
            //3.设置参数
            bootstrap.group(group)
                    //传输数据用的channel
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            //String类型编解码器
//                            pipeline.addLast(new RpcEncoder(RpcRegisterEntity.class,new JSONSerializer()));
//                            //解码器
//                            pipeline.addLast(new RpcDecoder(RpcRegisterEntity.class,new JSONSerializer()));
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            //添加客户端处理类
//                            pipeline.addLast(new RpcConsumerHandler());
                            pipeline.addLast(rpcConsumerHandler);
                        }
                    });
            //4.连接Netty服务端
            connect(bootstrap, ip, port, 5);
        } catch (Exception exception) {
            exception.printStackTrace();
            if (channel != null) {
                channel.close();
            }
            if (group != null) {
                group.shutdownGracefully();
            }
        }
    }

    private void connect(Bootstrap bootstrap, String host, int port, int retry) {
        ChannelFuture channelFuture = bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                logger.info("连接服务端成功");
            } else if (retry == 0) {
                logger.error("重试次数已用完，放弃连接");
            } else {
                //第几次重连：
                int order = (5 - retry) + 1;
                //本次重连的间隔
                int delay = 1 << order;
                logger.error("{} : 连接失败，第 {} 重连....", new Date(), order);
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
        channel = channelFuture.channel();
    }
    /**
     * 提供给调用者主动关闭资源的方法
     */
    public void close() {
        if (channel != null) {
            channel.close();
        }
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    /**
     * 提供消息发送的方法
     */
    public Object send(String msg) throws ExecutionException, InterruptedException {
        rpcConsumerHandler.setRequestMsg(msg);
        Future submit = executorService.submit(rpcConsumerHandler);
        return submit.get();
    }

    public void destroy() throws Exception {
        if (channel != null) {
            channel.close();
        }
        if (group != null) {
            group.shutdownGracefully();
        }
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
