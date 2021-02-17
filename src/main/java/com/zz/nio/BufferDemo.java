package com.zz.nio;

import java.nio.IntBuffer;

public class BufferDemo {

    public static void main(String[] args) {
        //说明buffer的使用

        //创建一个buffer 大小为5 5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);
        //存放数据
        for (int i = 0; i < 2; i++) {
            intBuffer.put(i);
        }

        //读数据，需要将buffer转换，读写切换

        intBuffer.flip();

        while(intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
