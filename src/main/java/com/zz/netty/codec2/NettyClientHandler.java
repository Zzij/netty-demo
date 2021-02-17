package com.zz.netty.codec2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //通道建立时触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

//        MyDataInfo.MyMessage myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.StudentType)
//                .setStudent(MyDataInfo.Student.newBuilder().setAge(12).setId(2).setName("zzj 小健").build()).build();
        MyDataInfo.MyMessage myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.WorkerType)
                .setWorker(MyDataInfo.Worker.newBuilder().setId(123).setDepartment("采矿部").setName("老王").build()).build();
        ctx.writeAndFlush(myMessage);
    }


    //当通道有读取事件时触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("收到消息： " + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器地址是 : " + ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("read complete");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("出现异常 关闭通道");
        ctx.close();
    }
}
