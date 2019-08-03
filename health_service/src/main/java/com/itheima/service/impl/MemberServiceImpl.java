package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    /*
    * 添加会员
    * */
    @Override
    public Member doLogin(String telephone) {
        return memberDao.doLogin(telephone);
    }

    /*
    * 执行登录
    * */
    @Override
    public void addMember(Member mem) {
        //没有密码设置密码
        if (mem.getPassword() != null) {

            mem.setPassword(MD5Utils.md5(mem.getPassword()));
        }
        memberDao.addMember(mem);

    }



    /*
    *
    * 统计到当前为止,一年内,会员的总数量,每个月统计一次,看会会员的增加情况
    * 封装两个数据:会员数量,以及月份,到map,返回给前端
    *
    * */
    @Override
    public Map<String, Object> getCountMember() {


        //1 获取上一年的时间,拿到日历对象默认是获取系统当日历,默认通过年月日获取当前的年月日;
        //我们需要设置set成我们需要的年月日
        Calendar calendar = Calendar.getInstance();
        //2 年的值,减1,就是去年
        calendar.add(Calendar.YEAR,-1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        ArrayList<String> months = new ArrayList();
        ArrayList<Integer> memberCount = new ArrayList();
        //3 遍历,拿到每一个月,注意是12次循环是操作同一个日历对象
        String month = "";
        for (int i = 0; i < 12; i++) {
            //这个月的最后一天,等于下个月减去1天
            //前面的操作,对象的年,已经减1,变成去年;
            //每循环一次,月份要加1 ,这的1就是这个意思,一直加到12个月.
            // 外国的0表示中国的1月,操加1 变成当前月
            calendar.add(Calendar.MONTH, 1);
            /*
            //学习一种获取某个月的最后的一天的方法;
            //再加1 ,变成下一个月,下一个月减去一天,就是这个月的最后一天
            calendar.add(Calendar.MONTH, 1);
            //这个月的最后一天
            //把当前月加进集合,并且值格式化到年月
            calendar.add(Calendar.DAY_OF_MONTH, -1);*/
            month=sdf.format(calendar.getTime());
            months.add(month);
            //在通过月去查询;
           Integer count= memberDao.findMemberCount(month+"-31");
           memberCount.add(count);
       }

        //循环结束之后,我们拿到两个集合,一个装的月份,一个装的人数;
        HashMap<String,Object> reportMap = new HashMap();
        reportMap.put("memberCount", memberCount);
        reportMap.put("months", months);

        return reportMap;
    }



    public static void main(String[] args) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH, 1);
        System.out.println(instance.get(Calendar.MONTH));


    }
}
