package com.iven.provider;

import com.iven.netty.JSONSerializer;
import com.iven.netty.RpcDecoder;
import com.iven.netty.RpcEncoder;
import com.iven.util.entity.RpcRegisterEntity;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;

public class NettyServer{
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private String serverAddress; //启动地址
    private int serverPort; //启动端口

    private EventLoopGroup boss = null;
    private EventLoopGroup worker = null;

    public NettyServer(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void startNettyServer() throws Exception {
        //netty调度模块，负责接收请求
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //netty调度模块，负责处理请求
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        //启动类
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                //传输数据的channel
                ChannelPipeline pipeline = ch.pipeline();
                //解码器
                pipeline.addLast(new StringDecoder());
                //编码器
                pipeline.addLast(new StringEncoder());
                //业务逻辑
                pipeline.addLast(new RpcServerHandler());
            }
        });

        try {
            //端口绑定
            ChannelFuture sync = bootstrap.bind(serverAddress, serverPort).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


    @PreDestroy
    public void destory() throws InterruptedException {
        boss.shutdownGracefully().sync();
        worker.shutdownGracefully().sync();
        logger.info("关闭Netty");
    }
}