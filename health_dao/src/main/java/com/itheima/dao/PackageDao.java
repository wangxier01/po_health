package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Package;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PackageDao {


    /*
     * 分页展示套餐信息
     * */
    Page<Package> findPage(String queryString);

    /*
     * 实现添加套餐
     *
     * */
    void addpackage(Package pkg);


    /*
     * 添加套餐的时候,维护关系表
     * */
    void addLinkTable(HashMap<String, Integer> hm);

    //删除套餐
    void deletePackage(Integer groupId);

    //删除套餐相关的检查组
    void deletePackageAndLink(Integer groupId);

    Package findPackageById(Integer id);

    List<Integer> findPackageGroup(Integer id);

    //编辑套餐
    void handleEdit(Package pkg);

    @Delete("delete from t_package_checkgroup where package_id = #{package_id}")
    void handleEditDelete(Integer package_id);

    //维护套餐检查组的关系表
    void handleLinkTable(@Param("package_id") Integer package_id, @Param("checkgroup_id") Integer checkgroup_id);

    /*
     * 移动端展示所有套餐
     * */
    List<Package> findAll();

    /*
     * 根据id,查询套餐详情
     *
     * */
    Package findById(Integer id);

    /*
     * 预约页面,展示套餐的基本信息
     * */
    Package InfoPackageById(int id);


    //查询套餐名
    @Select("select name from t_package where id = ${value} ")
    String findNameByid(Integer packageId);

    /*
     * 套餐预约占比
     * */
    ArrayList<Map<String, Object>> findPackageCount();
}
