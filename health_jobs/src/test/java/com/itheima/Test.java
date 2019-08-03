package com.itheima;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

    //测试我们写的任务调度
    //Quartz框架的使用
    public static void main(String[] args) {

        ApplicationContext  act=new ClassPathXmlApplicationContext("applicationContext-jobs.xml");
    }
}
