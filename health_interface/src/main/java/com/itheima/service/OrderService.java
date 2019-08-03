package com.itheima.service;

import com.sun.xml.internal.ws.handler.HandlerException;

import java.util.Map;

public interface OrderService {

    Integer addOrder(Map<String, String> orderInfo) throws Exception;

    Map<String, Object> findByIdOrder(Integer id);
}
