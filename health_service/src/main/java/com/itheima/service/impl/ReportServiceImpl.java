package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.service.ReportService;
import com.itheima.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/*
* 加事务,得指定接口
*
* */
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /*
     *
     * 获取运营数据的
     * */
    @Override
    public Map<String, Object> getBusinessReportData() {

        //1 拿到当前的时间,
        String reportDate = DateUtils.parseDate2String(DateUtils.getToday());
        //2 查询本日会员新增数,日期为今天,的会员数;
        Integer todayNewMember= memberDao.findMemberCountToday(reportDate);
        //3 当前的总会员数;
        Integer totalMember = memberDao.findTotalMemberCountToday();
        //4 本周新增加会员数;从这周的星期一,到现在,所增加的会员数;
        String monday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        Integer thisWeekNewMember = memberDao.findmenberCountThisWeek(monday,reportDate);
        //5 本月新增会员,本月的第一天,到今天
        String firstDay = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        Integer thisMonthNewMember =memberDao.findMemberCountThisMonth(firstDay,reportDate);
        //6今日预约的人数,这个在订单表查
        Integer todayOrderNumber=orderDao.findCountMemberOrderToday(reportDate);

        //7查询今日到诊的人数,就是那个预约的是今天,并且状态已经为已到诊;
        Integer todayVisitsNumber = orderDao.findCountVistedToday(reportDate,"已到诊");

        //8 本周预约的人数,从周一到周五;
        String sunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
        Integer thisWeekOrderNumber = orderDao.findCountthisWeekOrderNumber(monday,sunday);

        //9 本周到诊人数
        Integer thisWeekVisitsNumber = orderDao.findCountthisWeekVisitsNumber(monday,reportDate);

        //10 本月预约数,本月第一天,到月底的;
        String lastDayOfmonth = DateUtils.parseDate2String(DateUtils.getLastDayOfThisMonth());

        Integer thisMonthOrderNumber = orderDao.findCountthisMonthOrderNumber(firstDay,lastDayOfmonth);

        //11 统计本月的到诊人数;
        Integer thisMonthVisitsNumber = orderDao.findCountthisMonthVisitsNumber(firstDay,reportDate);

        /*
        * 12 热门套餐,预约次数排行前四,占比和remark;
        * 分析:1 要查询套餐表,拿到套餐名,和套餐的remark;
        *      2 查询订单表,拿到预约为这哥套餐的次数,拿到统计出预约总次数和预约这个套餐的次数计算占比,降序排序,取前四
        *      3 返回值是个map
        * */
        List<Map<String,Object>> hotPackage=orderDao.findhotPackageDetail();

        //1 reportData对应的是一个map
         HashMap<String,Object>  reportDatemap = new HashMap<>();

         reportDatemap.put("reportDate",reportDate);
         reportDatemap.put("todayNewMember", todayNewMember);
         reportDatemap.put("totalMember", totalMember);
         reportDatemap.put("thisWeekNewMember", thisWeekNewMember);
         reportDatemap.put("thisMonthNewMember", thisMonthNewMember);
         reportDatemap.put("todayOrderNumber", todayOrderNumber);
         reportDatemap.put("todayVisitsNumber", todayVisitsNumber);
         reportDatemap.put("thisWeekOrderNumber", thisWeekOrderNumber);
         reportDatemap.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
         reportDatemap.put("thisMonthOrderNumber", thisMonthOrderNumber);
         reportDatemap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
         reportDatemap.put("hotPackage", hotPackage);
        return reportDatemap;
    }


    public static void main(String[] args) {
        /*String reportDate = DateUtils.parseDate2String(DateUtils.getToday());
        System.out.println(reportDate);*/

        //System.out.println(DateUtils.parseDate2String(DateUtils.getThisWeekMonday()));
        System.out.println(DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth()));


    }
}

