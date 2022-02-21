package com.iven.consumer;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.Callable;


public class RpcConsumerHandler extends SimpleChannelInboundHandler<String> implements Callable {
    ChannelHandlerContext context;
    //发送的消息
    String requestMsg;

    //服务端返回的消息
    String responseMsg;

    public void setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }
    //接收由服务端返回的数据
    @Override
    protected synchronized void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        System.out.println("客户端结果："+msg);
        responseMsg = msg;
        //唤醒等待的线程
        notify();
    }
    //发送数据
    @Override
    public synchronized Object call() throws Exception {
        //消息发送
        context.writeAndFlush(requestMsg);
        //线程等待
        wait();
        return responseMsg;
    }
}
