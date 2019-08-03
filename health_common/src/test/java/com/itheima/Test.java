package com.itheima;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.util.SMSUtils;

import java.util.Random;

public class Test {

    public static void main(String[] args) throws ClientException {
       //短信测试
        SMSUtils.sendShortMessage("SMS_171356286","17722473618",new Random().nextInt(1000)+"");

    }
}
