package com.zz.nio;




import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class FileChannelDemo {

    public static void main(String[] args) throws Exception {
        gatheringAndScattering();
    }

    public static void fileChannelWrite() throws IOException {
        String str = "hello im zzj";

        FileOutputStream fileOutputStream = new FileOutputStream(new File("D://test.txt"));
        //获取channel
        FileChannel channel = fileOutputStream.getChannel();
        //将string写入buffer中

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        byteBuffer.put(str.getBytes());

        //进行反转
        byteBuffer.flip();
        channel.write(byteBuffer);
        fileOutputStream.close();
    }

    public static void fileChannelRead() throws IOException{
        FileInputStream fileInputStream = new FileInputStream(new File("D://test.txt"));

        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(50);
        int read = channel.read(buffer);

        buffer.flip();

        System.out.println(new String(buffer.array()));

        fileInputStream.close();
    }

    public static void copyFile() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);

        while(true){
            //需要重置 position 让bytebuffer可以写入
            byteBuffer.clear();

            int read = inputStreamChannel.read(byteBuffer);
            if(read == -1){
                break;
            }

            byteBuffer.flip();
            outputStreamChannel.write(byteBuffer);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }

    public static void copyFileByTransfer() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel inputStreamChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        inputStreamChannel.transferTo(0, inputStreamChannel.size(), outputStreamChannel);

        fileInputStream.close();
        fileOutputStream.close();
    }

    public static void mapperByteBufferP() throws Exception{
        //mapperByteBuffer 在内存操作 零拷贝 直接内存

        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt", "rw");

        FileChannel channel = randomAccessFile.getChannel();
        System.out.println(channel.size());
        ByteBuffer allocate = ByteBuffer.allocate(20);

        channel.read(allocate);
        allocate.flip();
        System.out.println(new String(allocate.array(), "utf-8"));
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(2, (byte) '8');
        randomAccessFile.close();

    }


    public static void gatheringAndScattering() throws IOException {

        //scattering :将数据写入到buffer，可以采用buffer数组，依次写入
        //gathering：从buffer读取数据，可以采用buffer数组依次读

        //开启serversocketchannel 绑定地址端口
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8000);
        serverSocketChannel.bind(inetSocketAddress);
        SocketChannel socketChannel = serverSocketChannel.accept();

        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];

        byteBuffers[0] = ByteBuffer.allocate(5);

        byteBuffers[1] = ByteBuffer.allocate(5);


        while(true){
            long read = socketChannel.read(byteBuffers);
            System.out.println("read length " + read);
            Arrays.asList(byteBuffers).stream().map(buffer -> "position=" + buffer.position() + " limit=" + buffer.limit()).forEach(System.out::println);

            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            socketChannel.write(byteBuffers);

            Arrays.asList(byteBuffers).forEach(buffer -> buffer.clear());
        }

    }
}
