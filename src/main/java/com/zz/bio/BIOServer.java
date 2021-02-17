package com.zz.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {

    //1 创建一个线程池

    //2 如果有客户端连接，就创建一个线程与之通信

    public static void main(String[] args) throws IOException {

        ExecutorService threadPool = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(8480);
        while(true){
            final Socket socket = serverSocket.accept();
            threadPool.execute(new Runnable() {
                public void run() {
                    hanler(socket);
                }
            });
        }
    }

    public static void hanler(Socket socket){
        try (InputStream inputStream = socket.getInputStream()){

            byte[] bytes = new byte[1024];
            while(true){
                int read = inputStream.read(bytes);
                if(read != -1){
                    System.out.println(Thread.currentThread().getName() + "  " + new java.lang.String(bytes));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
