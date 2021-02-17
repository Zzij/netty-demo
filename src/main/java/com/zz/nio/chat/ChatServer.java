package com.zz.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class ChatServer {

    private Selector selector;

    private ServerSocketChannel listenChannel;

    public static final int port = 6667;

    public ChatServer(){
        try{
            selector = Selector.open();

            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(port));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void listen(){

        //监听事件

        try{
            while(true){
                int count = selector.select(2000);
                if(count >0 ){
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if(key.isAcceptable()){
                            System.out.println(1);
                            SocketChannel socketChannel = (SocketChannel) listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            //提示上线
                            System.out.println(socketChannel.getRemoteAddress() + " 上线了！");
                        }

                        if(key.isReadable()){
                            readData(key);
                        }

                        iterator.remove();
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readData(SelectionKey key){
        SocketChannel socketChannel = null;

        try{
            socketChannel = (SocketChannel) key.channel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            int read = 1;
            while((read = socketChannel.read(byteBuffer)) > 0){
                String s = new String(byteBuffer.array());
                System.out.println(s);
                sendMsg(s, socketChannel);
            }

        }catch (Exception e){
            e.printStackTrace();
            try {
                System.out.println(socketChannel.getRemoteAddress() + " 下线了");
                //取消注册 关闭channel
                key.cancel();
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendMsg(String msg, SocketChannel self) throws IOException {
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys) {
            Channel channel = key.channel();
            //排除自己  且要是socketchannel
            if(channel instanceof SocketChannel && channel != self){
                ((SocketChannel) channel).write(ByteBuffer.wrap(msg.getBytes()));
            }
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.listen();
    }
}
