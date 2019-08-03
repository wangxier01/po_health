package com.itheima.dao;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Select;

public interface UserDao {

    /*
    * 根据用户名查询用户
    * */

   @Select("select * from t_user where username = #{username}")
    User findUserByName(String username);
}
