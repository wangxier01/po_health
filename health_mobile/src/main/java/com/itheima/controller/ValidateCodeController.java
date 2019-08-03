package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.util.SMSUtils;
import com.itheima.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RequestMapping("/validateCode")
@RestController
public class ValidateCodeController {

    /*
     * 处理发送预约的验证码请求,
     * 接收参数:手机号
     * */
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {

        //调用工具,发送验证码,验证码需要存在redis里面
        //1 生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(6);

        //2 发送验证码
        try {
            //传过去是串串,tostring或者,加空串
            SMSUtils.sendShortMessage("SMS_171356286", telephone, code + "");
            //3 发送成功,验证码存在jedis
            Jedis jedis = jedisPool.getResource();

            /*存
            * setex方法的特点是:存指定的时间,自动清除;
            * 当我们存验证码的时候,要注意这个key,要唯一区分;
            * */
            jedis.setex(telephone+RedisMessageConstant.SENDTYPE_ORDER, 5*10000, code+"");
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();

        }

        return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {

        //调用工具,发送验证码,验证码需要存在redis里面
        //1 生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(6);

        //2 发送验证码
        try {
            //传过去是串串,tostring或者,加空串
            SMSUtils.sendShortMessage("SMS_171356286", telephone, code + "");
            //3 发送成功,验证码存在jedis
            Jedis jedis = jedisPool.getResource();

            /*存
             * setex方法的特点是:存指定的时间,自动清除;
             * 当我们存验证码的时候,要注意这个key,要唯一区分;
             * */
            jedis.setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN, 5*10000, code+"");
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();

        }

        return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
    }


}
