package com.itheima.dao;

import com.itheima.pojo.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface PermissionDao {

    Set<Permission> findpermissionById(Integer roleId);
}
