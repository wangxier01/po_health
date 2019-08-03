package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CheckGroupDao  {
    Integer addgroup(CheckGroup checkGroup);

    /*
    * 新建检查组的时候添加关系表
    * */
    void addItemAndGroup(HashMap<String, Integer> hm);

    List<CheckGroup> findPage(String queryString);

    Page<CheckGroup> findAllBycondition(String queryString);

    CheckGroup findGroup(Integer groupId);

    List<Integer> findGroupItems(Integer id);

    void updateGroup(CheckGroup checkGroup);

    void deleteByGroupId(Integer checkgroupId);

    /*
    *
    * 修改检查组的时候,先删除,后维护关系表
    * */
    void addLinkItemAndGroup(@Param("checkgroupId") Integer checkgroupId, @Param("checkitemId") Integer checkitemId);

    List<CheckGroup> findAll();


}
