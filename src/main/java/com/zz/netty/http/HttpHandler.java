package com.zz.netty.http;

import com.sun.net.httpserver.HttpsParameters;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class HttpHandler extends SimpleChannelInboundHandler<HttpObject> {

    //httpObject客户端和服务端相互通信的数据封装成httpObject

    //读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {

        //判断msg是不是httprequest请求

        if(httpObject instanceof HttpRequest){

            System.out.println("请求类型是： " + httpObject.getClass());
            System.out.println("请求地址是： " + channelHandlerContext.channel().remoteAddress());

            //回复信息给浏览器

            ByteBuf byteBuf = Unpooled.copiedBuffer("hello moto 你好啊".getBytes(CharsetUtil.UTF_8));
            //构造一个httpresponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

            channelHandlerContext.writeAndFlush(response);

        }
    }
}
