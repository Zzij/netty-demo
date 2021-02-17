package com.zz.netty.codec2;

import com.zz.netty.codec.StudentPOJO;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //读取student

        MyDataInfo.MyMessage message = (MyDataInfo.MyMessage) msg;
        MyDataInfo.MyMessage.DataType dataType = message.getDataType();

        if(dataType.equals(MyDataInfo.MyMessage.DataType.StudentType)){
            System.out.println("学生");
            MyDataInfo.Student student = message.getStudent();
            System.out.println("收到数据 id:" + student.getId() + ", name" + student.getName() + ", age" + student.getAge());
        }else if(dataType.equals(MyDataInfo.MyMessage.DataType.WorkerType)){
            System.out.println("g工人");
            MyDataInfo.Worker worker = message.getWorker();
            System.out.println("收到数据 id:" + worker.getId() + ", name" + worker.getName() + ", department" + worker.getDepartment());
        }else {
            System.out.println("无法解析");
        }

    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 你好", CharsetUtil.UTF_8));
    }


    //发生异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
