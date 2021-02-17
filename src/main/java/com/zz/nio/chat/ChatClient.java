package com.zz.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class ChatClient {

    private static final String host = "127.0.0.1";
    private static final int port = 6667;

    private SocketChannel socketChannel;

    private Selector selector;

    private String username;

    public ChatClient(){
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            username = socketChannel.getLocalAddress().toString();
            System.out.println(username + " client is ok...");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMsg(String msg){
        msg = username + " send: " + msg;
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
            System.out.println("发送成功");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("发送失败");
        }
    }

    public void readData(){
        try{
            while(true){
                int select = selector.select(2000);
                if(select > 0){
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if(key.isReadable()){
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            int read = 1;
                            while((read = channel.read(byteBuffer)) > 0){
                                String s = new String(byteBuffer.array());
                                System.out.println("收到消息： " + s);
                            }
                        }
                        iterator.remove();
                    }

                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatClient.readData();
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String s = scanner.nextLine();
            chatClient.sendMsg(s);
        }
    }

}
