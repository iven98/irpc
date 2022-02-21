package com.iven.provider;

import com.iven.netty.JSONSerializer;
import com.iven.netty.RpcDecoder;
import com.iven.netty.RpcEncoder;
import com.iven.provider.RpcServerHandler;
import com.iven.util.entity.RpcRegisterEntity;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class RpcServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 12, 4, 0, 0))
//                .addLast(new RpcDecoder(RpcRegisterEntity.class,new JSONSerializer()))
//                .addLast(new RpcEncoder(RpcRegisterEntity.class,new JSONSerializer()))
                .addLast(new StringEncoder())
                .addLast(new StringDecoder())
                .addLast(new RpcServerHandler());
    }
}
