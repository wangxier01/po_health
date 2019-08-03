package com.itheima.dao;

import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface OrderDao {


    /*
    * 执行预约
    * 在订单表插入
    * */
    void AddOrder(Order order);


    List<Order> findOderSetting(Order order);


    @Select("select * from t_order where id = ${value}")
    Map<String, Object> findOrderByid(Integer id);


    /*
    *
    * 统计预约日期为今天的人数
    * */
    @Select("select count(1) from t_order where orderDate = '${value}'")
    Integer findCountMemberOrderToday(String reportDate);


    /*
    *
    * 统计今天到诊的人数
    * */
    @Select("select count(1) from t_order where orderDate = #{reportDate} and orderStatus = #{status}")
    Integer findCountVistedToday(@Param("reportDate") String reportDate, @Param("status") String status);

    /*
    *
    * 本周的预约人数
    * */
    @Select("select count(1) from t_order where orderDate between #{monday} and #{reportDate}")
    Integer findCountthisWeekOrderNumber(@Param("monday") String monday, @Param("reportDate") String reportDate);

    /*
    * 统计本周到诊人数
    *
    * */
    @Select("select count(1) from t_order where orderDate between #{monday} and #{reportDate} and orderStatus ='已到诊'")
    Integer findCountthisWeekVisitsNumber(@Param("monday") String monday, @Param("reportDate")String reportDate);

    /*
    * 统计本月的预约数
    * */
    @Select("select count(1) from t_order where orderDate between #{firstDay} and #{reportDate}")
    Integer findCountthisMonthOrderNumber(@Param("firstDay") String firstDay, @Param("reportDate") String reportDate);


    @Select("select count(1) from t_order where orderDate between #{firstDay} and #{reportDate} and orderStatus ='已到诊'")
    Integer findCountthisMonthVisitsNumber(@Param("firstDay") String firstDay, @Param("reportDate") String reportDate);


    /*
    *
    * 统计热门套餐;
    * */

    List<Map<String, Object>> findhotPackageDetail();
}
