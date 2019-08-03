package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

    public void add(CheckItem checkItem);

   public PageResult findpage(QueryPageBean queryPageBean);

    void delete(Integer id);

    CheckItem findOneById(Integer id);

    void updateOne(CheckItem checkItem);

    List<CheckItem> findAllShow();


}
