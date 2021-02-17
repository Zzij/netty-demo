package com.zz.netty.inboundoutbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {


    /**
     *
     * @param ctx  上下文
     * @param in  收到的字节
     * @param out  解码的集合
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        //将byte读为long  一个long八个byte
        System.out.println("解码");
        if(in.readableBytes() >= 8){
            out.add(in.readLong());
        }
    }
}
