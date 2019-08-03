package com.itheima.dao;

import com.itheima.pojo.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface MemberDao {


    Member checkIsNoMember(String telephone);

    void InsertRemeber(Member member);

    @Select("select name from t_member where id = ${value}")
    String findMemberName(Integer memberId);


    /*
    * 登录
    * */

    @Select("select * from t_member where phoneNumber = ${value}")
    Member doLogin(String telephone);

    /*
    * 添加会员,需要键入id就加下面的
    *  @Options(useGeneratedKeys = true,keyColumn = "id", keyProperty = "id")
    * */
    @Insert("insert into t_member (phoneNumber,regTime) values (#{phoneNumber},#{regTime})")
    void addMember(Member mem);

    /*
    * 按照月份,统计会员的总数量
    * */
    @Select("select count(1) from t_member where regTime <= #{value}")
    Integer findMemberCount(String s);


    /*
    统计
    * 今天会员的新增加数量
    * */
    @Select("select count(1) from t_member where regTime = #{value}")
    Integer findMemberCountToday(String reportDate);

    /*
    *
    * 统计会员的总数
    * */
    @Select("select count(1) from t_member")
    Integer findTotalMemberCountToday();

    /*
    * 统计本周新增的会员
    * */
    @Select("select count(1) from t_member where regTime between #{monday} and #{reportDate}")
    Integer findmenberCountThisWeek(@Param("monday") String monday, @Param("reportDate") String reportDate);


    /*
    * 统计本月新增会员数量
    * */
    @Select("select count(1) from t_member where regTime between #{firstDay} and #{reportDate}")
    Integer findMemberCountThisMonth(@Param("firstDay") String firstDay, @Param("reportDate") String reportDate);
}
