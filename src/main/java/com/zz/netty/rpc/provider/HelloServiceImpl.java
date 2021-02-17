package com.zz.netty.rpc.provider;

import com.zz.netty.rpc.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String str) {
        System.out.println("receive " + str);
        return "hello " + str;
    }


}
