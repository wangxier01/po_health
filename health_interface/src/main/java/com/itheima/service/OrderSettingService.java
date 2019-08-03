package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void add(ArrayList<OrderSetting> ordersettingList);

    List<Map> findOrdersettingByMonth(String date);

    void setNumPerson(OrderSetting map);
}
