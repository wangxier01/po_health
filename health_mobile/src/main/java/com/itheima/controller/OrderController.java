package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.exception.HealthException;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.sun.xml.internal.ws.handler.HandlerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    /*
     * 接收与订单相关的业务请求的
     * 预约提交,参数为:
     *   1 预定的套餐id
     *   2 用户信息;姓名,性别,手机号,身份证号
     *   3 预约的体检日期;
     * */

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;


    @RequestMapping("/submit")
    public Result submit(@RequestBody Map<String, String> orderInfo) throws Exception {

        //1使用map接收所有的输入的参数;
        //也可以使用实体接收所有参数;属性与参数名一一对应即可;
        //2 校验验证码是否正确;

        Jedis jedis = jedisPool.getResource();
        String Rcode = jedis.get(orderInfo.get("telephone") + RedisMessageConstant.SENDTYPE_ORDER);

        //验证码不能为空
        if (Rcode == null) {

            return new Result(false, MessageConstant.PLEASE_VALIDATECODE_INPUT);
        }
        String code = orderInfo.get("validateCode");
        if (!Rcode.equals(code)) {

            return new Result(false, MessageConstant.VALIDATECODE_ERROR);

        }
        /*
         * 如果是电话预约,就是写电话预约
         * 如果是微信预约,就写微信预端
         * */
        orderInfo.put("orderType", "微信端");

        Integer orderId = null;
        /*yichangchuliqi*/
        orderId = orderService.addOrder(orderInfo);
        return new Result(true, MessageConstant.ORDER_SUCCESS, orderId);


    }

    /*
     * 根据订单id查询,订单详情;
     *
     * */
    @PostMapping("/findById")
    public Result findByIdOrder(Integer id) {

        try {
            Map<String, Object> map = orderService.findByIdOrder(id);

            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS, map);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false);
        }


    }


}
