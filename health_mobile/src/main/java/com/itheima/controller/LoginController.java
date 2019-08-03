package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.exception.HealthException;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.util.SMSUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RequestMapping("/login")
@RestController
public class LoginController {

    @Reference
    private MemberService memberService;

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/check")
    public Result MemberCheck(HttpServletResponse response, @RequestBody Map<String, String> loginInfo) {

        String telephone = loginInfo.get("telephone");

        //1 根据手机号,拿到验证码;
        Jedis jedis = jedisPool.getResource();
        String code = jedis.get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (code == null) {
            return new Result(false, MessageConstant.PLEASE_VALIDATECODE_INPUT);
        }

        //2 获取输入的验证码;
        String validateCode = loginInfo.get("validateCode");
        if (validateCode == null) {
            return new Result(false, MessageConstant.PLEASE_VALIDATECODE_INPUT);
        }
        //如果验证码不正确
        if (!validateCode.equals(code)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        //验证码正确了,调用业务,将会员信息,存放session域对象
        Member member = memberService.doLogin(telephone);
        if (member == null) {
            // 不是会员,注册会员;
            Member mem = new Member();
            mem.setPhoneNumber(telephone);
            mem.setRegTime(new Date());

            memberService.addMember(mem);
        }
        //执行到这的时候,member已经是会员,将手机号码存在cookie对象,方便以后做统计分析;
        Cookie member_cookie = new Cookie("login_member_telephone", telephone);
        member_cookie.setPath("/"); //设置为网站的根路径
        member_cookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(member_cookie); //将ccokie有响应带给客户端

        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }


}
