package com.zz.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import jdk.jfr.events.SocketReadEvent;

import java.util.concurrent.TimeUnit;

public class ChatServer {

    private int port;

    public ChatServer(int port){
        this.port = port;
    }

    public void run() throws InterruptedException {
        //创建线程组
        EventLoopGroup bossEventGroup = new NioEventLoopGroup();
        NioEventLoopGroup workEventGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossEventGroup, workEventGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .handler(new LoggingHandler(LogLevel.INFO)) //给bossgroup增加处理器
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //加入解码器
                        ch.pipeline().addLast("decoder", new StringDecoder());
                        //加入编码器
                        ch.pipeline().addLast("encoder", new StringEncoder());

                        /**
                         * IdleStateHandler netty提供的空闲检测机制
                         @param readerIdleTimeSeconds  多长时间没有读事件 就发送心跳包检测
                          *        an {@link IdleStateEvent} whose state is {@link IdleState#READER_IDLE}
                          *        will be triggered when no read was performed for the specified
                          *        period of time.  Specify {@code 0} to disable.
                          * @param writerIdleTimeSeconds 多长时间没有写事件 就发送心跳包检测
                         *        an {@link IdleStateEvent} whose state is {@link IdleState#WRITER_IDLE}
                         *        will be triggered when no write was performed for the specified
                         *        period of time.  Specify {@code 0} to disable.
                         * @param allIdleTimeSeconds 多长时间没有读写事件 就发送心跳包检测
                         *        an {@link IdleStateEvent} whose state is {@link IdleState#ALL_IDLE}
                         *        will be triggered when neither read nor write was performed for
                         *        the specified period of time.  Specify {@code 0} to disable.
                         *
                         *
                         */
                        //当这个事件触发后 会给下个handler处理  该方法userEventTriggered
                        ch.pipeline().addLast(new IdleStateHandler(3, 5, 6, TimeUnit.SECONDS));
                        //业务处理器
                        ch.pipeline().addLast(new ChatServerHandler());
                    }
                });

        System.out.println("服务器准备完毕正在启动");
        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();


        channelFuture.channel().closeFuture().sync();


    }

    public static void main(String[] args) throws InterruptedException {
        ChatServer chatServer = new ChatServer(8550);
        chatServer.run();
    }
}
