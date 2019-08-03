package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Package;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PackageService {
    PageResult findPage(QueryPageBean queryPageBean);

    void addpackage(Package pkg, Integer[] checkgroupIds) throws Exception;

    void deletePackage(Integer groupId) throws Exception;

    Package findPackageById(Integer id);


    List<Integer> findPackageGroup(Integer id);

    void handleEdit(Package pkg, Integer[] checkgroupIds);


    /*
    * 移动端展示所有套餐
    * */
    List<Package> findAll();


    /*
    *
    *根据套餐id,查询套餐详情,:
    *
    *
    * */
    Package findById(Integer id);


    /*
    *
    * 这个是查询套餐的基本信息
    * */
    Package InfoPackageByid(int id);

    /*
    *
    * 套餐预约占比
    * */
    ArrayList<Map<String, Object>> findPackageCount();
}
