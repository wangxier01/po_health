package com.itheima.controller;

import com.itheima.entity.Result;

import com.itheima.exception.HealthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;




@ControllerAdvice
public class GlobalExceptionHandler {
/*
  全局异常处理
* 纪录日志,logger.xml
* Controller调用service，service调用dao，异常都是向上抛出的，
* 最终有DispatcherServlet找异常处理器进行异常的处理。
* <bean class="com.itheima.controller.ExceptionHandler"></bean>
* 或者注解
* */

//这一句代码的作用是,日志;

    private  static  final Logger log = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /*
    * 自定义异常处理
    * */
    @ExceptionHandler(HealthException.class)
    @ResponseBody
    public Result HandlerHealthException(HealthException e){

        log.error("验证失败",e);

        return new Result(false,e.getMessage());
    }


    /*
    *
    * 全局异常处理
    * */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleeException(Exception e){

        log.error("出错了",e);
        return new Result(false,"出错了,请联系管理员");
    }


    /*
    *
    * 注意哦这个异常类导包不要倒错了
    * */
    @ResponseBody
    @ExceptionHandler(AccessDeniedException.class)
    public Result HandlerAccessExeption(AccessDeniedException e){

        log.error("访问的权限异常",e);
        return new Result(false,"您的权限不足");
    }


}
