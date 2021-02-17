package com.zz.netty.websocket;

import com.zz.netty.chat.ChatServer;
import com.zz.netty.chat.ChatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class WebSocketServer {
    private int port;

    public WebSocketServer(int port){
        this.port = port;
    }

    public void run() throws InterruptedException {
        //创建线程组
        EventLoopGroup bossEventGroup = new NioEventLoopGroup();
        NioEventLoopGroup workEventGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossEventGroup, workEventGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO)) //给bossgroup增加处理器
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //使用http解码器
                        ch.pipeline().addLast(new HttpServerCodec());
                        //以块方式读写
                        ch.pipeline().addLast(new ChunkedWriteHandler());
                        //http数据是分段的，HttpObjectAggregator可以将多个段聚合
                        ch.pipeline().addLast(new HttpObjectAggregator(8192));

                        //将按照websocket协议处理
                        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/"));

                        //websocket 一帧一帧处理内容的
                        ch.pipeline().addLast(new WebSocketServerHandler());
                    }
                });

        System.out.println("服务器准备完毕正在启动");
        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();


        channelFuture.channel().closeFuture().sync();


    }

    public static void main(String[] args) throws InterruptedException {
        WebSocketServer webSocketServer = new WebSocketServer(8550);
        webSocketServer.run();
    }

}
