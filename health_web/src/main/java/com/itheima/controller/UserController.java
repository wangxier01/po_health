package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;

    /*
     * 1 获取当前登录用户的用户名
     * */

    @RequestMapping("/getuserName")
    public Result getUsername() {

        /* 获取username的两种方式,
         * SecurityContextHolder,是存入上下文的内容;
         * getAuthentication(),操作是获取认证信息的userdetial
         *
         * */

        try {
            //1 获取
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = user.getUsername();
            System.out.println("方法一:"+username);

            //2
            username = SecurityContextHolder.getContext().getAuthentication().getName();


            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }

    /*
     *
     * 处理登录;
     * */


}
