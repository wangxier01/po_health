package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 预约设置,业务处理
 *
 * */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {


    @Autowired
    private OrderSettingDao orderSettingDao;

    /*
     * 用户上传的预约信息,批量添加预约
     * 参数:使用实体类,封装的日期,和可以预约人数
     *
     * */
    @Override
    @Transactional
    public void add(ArrayList<OrderSetting> list) {

        if (list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {

                //1 先检验这个日期,是否在存在
                Integer count = orderSettingDao.findDateByOrder(orderSetting.getOrderDate());

                if (count > 0) {
                    //2 日期已经存在,就要执行更新操作,用现在的数据覆盖之前的数据;
                    orderSettingDao.updateByOrderDate(orderSetting);
                } else {
                    //3 不存在,就执行添加操作
                    orderSettingDao.addOrderDate(orderSetting);
                }

            }


        }


    }

    /*
     *
     * 按月展示预约信息,
     *数据库查询到月的预约信息,封装在map,存放在集合,返回页面;
     *
     * */
    @Override
    public List<Map> findOrdersettingByMonth(String date) {

        //根据月,拿到月的第一天和最后一天
        String dateBegin = date + "-1";
        String dateEnd = date + "-31";
        //传一个范围,dao查询,查询能范湖一个集合么?mybatis只能返回一个list
        List<OrderSetting> list = orderSettingDao.findbyDate(dateBegin, dateEnd);
        /*创建map存键值,键是字段名,值是字段值*/
        ArrayList<Map> orderList = new ArrayList();
        for (OrderSetting orderSetting : list) {
            /*map的泛型可以不指定*/
                HashMap orderMap = new HashMap();
                /*注意日期格式与界面一致,值显示到date日*/
                orderMap.put("date", orderSetting.getOrderDate().getDate());
                orderMap.put("number", orderSetting.getNumber());
                orderMap.put("reservations", orderSetting.getReservations());

                orderList.add(orderMap);
        }
        return orderList;
    }

    /*
    * 设置可预约人数;
    * 收到参数:日期,人数
    *
    * */
    @Override
    public void setNumPerson(OrderSetting orderSetting) {

        //1 根据日期,查找当天
       Integer count = (Integer) orderSettingDao.findCountByDay(orderSetting.getOrderDate());
       //2 有就修改,无就添加,上一步有这个方法
        if (count>0) {
            orderSettingDao.updateByOrderDate(orderSetting);

        }else{
            //无就添加,这个方法需要写
            orderSettingDao.addDateNum(orderSetting);

        }



    }
}
