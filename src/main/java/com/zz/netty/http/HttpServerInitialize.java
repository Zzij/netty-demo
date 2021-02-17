package com.zz.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerInitialize extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //得到管道

        ChannelPipeline pipeline = socketChannel.pipeline();

        //加入netty提供的httpservercodec  编码器
        //httpserverCodec 是netty提供的 http解码器 编码器

        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());

        //增加自定义的handler
        pipeline.addLast("MyHttpHandler", new HttpHandler());
    }
}
