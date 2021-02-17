package com.zz.netty.rpc.provider;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("msg " + msg);

        //helloservice 调用的话 相应处理
        if(msg.toString().startsWith("HelloService#")){
            String hello = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().indexOf("HelloService#")));
            ctx.writeAndFlush(hello);
        }else{
            ctx.writeAndFlush("aaaa");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常退出");
        System.out.println(cause);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接来了");
    }
}
