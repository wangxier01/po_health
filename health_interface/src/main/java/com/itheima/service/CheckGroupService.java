package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    void addgroup(Integer[] id, CheckGroup checkGroup) throws Exception;

    PageResult findPage(QueryPageBean pageResult);

    CheckGroup findGroupItem(Integer groupId);

   List<Integer> findGroupItems(Integer id);

    void updatGroupAndLink(Integer[] checkitemIds, CheckGroup checkGroup);

    List<CheckGroup> findAll();

}
