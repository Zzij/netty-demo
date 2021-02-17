package com.zz.netty.rpc.consumer;

import com.zz.netty.rpc.publicinterface.HelloService;

public class NettyConsumer {

    public static final String providerName = "HelloService#";

    public static void main(String[] args) throws InterruptedException {

        NettyClient client = new NettyClient();

        HelloService helloService = (HelloService) client.getBean(HelloService.class, providerName);

        for (;;){
            Thread.sleep(1000);
            String result = helloService.hello("我是消费者");

            System.out.println(result);
        }

    }
}
