package com.zz.netty.inboundoutbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        //编码
        pipeline.addLast(new MyLongToByteEncoder());
        //自定义handler
        pipeline.addLast(new MyClientHandler());
    }
}
