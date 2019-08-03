package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;

public interface CheckItemDao {

    //基于Mybatis的mapper代理技术实现持久层操作,只需要提供接口和mapper映射
    //配置文件,不需要提供实现类

    public void add(CheckItem checkItem);


    Page<CheckItem> findAllBycondition(String queryString);

    Integer cheeckById(Integer id);

    void deleteById(Integer id);

    CheckItem finOneById(Integer id);

    void updateOne(CheckItem checkItem);

    List<CheckItem> findAllShow();

}
