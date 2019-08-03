package com.itheima.dao;

import com.itheima.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface OrderSettingDao {
    /*
    * 查询预约的时间是否存在,
    * 参数,是用户传过来的;
    * 返回值是查到的数据条数
    * */
    Integer findDateByOrder(Date orderDate);

    /*
    *
    * 如果已经查到日期存在,那就以现在的数据进行更新
    * 参数:实体类
    * */
    void updateByOrderDate(OrderSetting orderSetting);

    /*
    * 不存在,就添加
    * */
    void addOrderDate(OrderSetting orderSetting);

    /*
    * 按月展示日历
    * */
    List<OrderSetting> findbyDate(@Param("dateBegin") String dateBegin, @Param("dateEnd") String dateEnd);


    /*
    * 查某一天是否有预约
    * */
    @Select("select count(*) from t_ordersetting where orderDate=#{orderDate}")
    Integer findCountByDay(Date orderDate);

    /*
    * 有就更新预约
    * */

    /*
    * 无就添加预约
    * */
    void addDateNum(OrderSetting orderSetting);

    /*
    * 根据日期查,是否可用预约
    * */
    OrderSetting findOrderDate(String orderDate);

    /*
    * 预约成功之后,需要改变预约信息的表
    *
    * */
    void updateOrderNumber(@Param("num") int num, @Param("orderDate") String orderDate);
}
