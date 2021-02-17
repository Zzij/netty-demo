package com.zz.nio.demo.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {

    public static void main(String[] args) throws IOException {

        //创建serversocketchannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //创建selector实例
        Selector selector = Selector.open();

        //绑定端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress(4000);
        serverSocketChannel.bind(inetSocketAddress);
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //serversocketchannel注册到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //循环等待客户端连接
        while(true){
            //当前所有注册中selector上的channel
            System.out.println("selector keys " + selector.keys().size());

            //设置最大等待1s 返回监听到的事件
            if(selector.select(1000) == 0){
                System.out.println("等待1s，无连接");
                continue;
            }

            //若是有事件返回,获取selectorkeys
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //遍历key获取连接通道
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                //根据key，获取对应的channel事件处理
                //新的客户端连接
                if(selectionKey.isAcceptable()){
                    //获取连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    //将socketchannel，注册到selector,关注事件为OP_READ，同时给该channel关联个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("新的客户端连接");
                }

                //channel可读
                if(selectionKey.isReadable()){
                    //反向获取到channel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    //获取到该channel关联的buffer
                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                    socketChannel.read(byteBuffer);
                    System.out.println(new String(byteBuffer.array()));
                }
                //删除key
                iterator.remove();
            }

        }
    }
}
