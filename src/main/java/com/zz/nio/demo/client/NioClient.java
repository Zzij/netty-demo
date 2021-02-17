package com.zz.nio.demo.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {

    public static void main(String[] args) throws IOException {

        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //连接地址端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 4000);
        //连接服务器

        //连接失败
        if(!socketChannel.connect(inetSocketAddress)){

            while(!socketChannel.finishConnect()){
                System.out.println("正在连接中。。。。。。。。");
            }
        }
        //连接成功

        String str = "hello 你好";
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        socketChannel.write(byteBuffer);

        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
