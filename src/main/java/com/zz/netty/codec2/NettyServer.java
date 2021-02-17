package com.zz.netty.codec2;

import com.zz.netty.codec.StudentPOJO;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

public class NettyServer {

    public static void main(String[] args) throws InterruptedException {

        //创建bossgroup和workergroup
        //bossgroup处理请求  workgroup负责业务处理
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //创建服务器端的启动对象，配置参数

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup)  //设置线程组
                .channel(NioServerSocketChannel.class) //使用SocketChannel作为通道实现
                .option(ChannelOption.SO_BACKLOG, 128)   //设置线程队列得到连接个数
                .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() {   //创建一个通道测试对象
                    //给pipeline设置处理器
                    protected void initChannel(SocketChannel socketChannel) throws Exception {

                        socketChannel.pipeline().addLast(new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()));
                        socketChannel.pipeline().addLast(new NettyServerHandler());
                    }
                });
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
