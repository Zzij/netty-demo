package com.zz.netty.http;

import com.zz.netty.demo.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HttpServeNetty {


    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //创建服务器端的启动对象，配置参数

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup)  //设置线程组
                .channel(NioServerSocketChannel.class) //使用SocketChannel作为通道实现
                .childHandler(new HttpServerInitialize());


        ChannelFuture channelFuture = bootstrap.bind(6674).sync();

        //绑定监听器

        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()){
                    System.out.println("在端口6674监听成功");
                }
            }
        });

        //对关闭通道进行监听
        channelFuture.channel().closeFuture().sync();
    }
}
