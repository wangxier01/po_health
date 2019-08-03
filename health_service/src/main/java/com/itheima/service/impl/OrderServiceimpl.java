package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.dao.PackageDao;
import com.itheima.exception.HealthException;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Package;
import com.itheima.service.OrderService;
import com.itheima.service.PackageService;
import com.itheima.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
public class OrderServiceimpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceimpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private PackageDao packageDao;



    /*
     * 处理预约业务
     *
     * */
    @Override
    @Transactional
    public Integer addOrder(Map<String, String> orderInfo) throws HealthException {

        //1 先判断当前日期,是否可用预约?就是这个日期在预约的表单里面,有没有纪录;因为用户选择的日是日历随便写的;
        //查的是哪一张表?ordersetting表
        OrderSetting orderSetting = orderSettingDao.findOrderDate(orderInfo.get("orderDate"));

        log.info("123456789--yonghu yuyue xinxi:"+orderSetting);
        if (orderSetting == null) {
            //不能预约,报错
            throw new HealthException(MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2 判断当前日期预约人数是否已满;
        if (orderSetting.getReservations() >= orderSetting.getNumber()) {
            //如果人数已满,也不能预约,报错;
            throw new HealthException(MessageConstant.ORDER_FULL);
        }
        //3 通过手机号或者身份证判断是不是会员,查询的是会员表
        Member member = memberDao.checkIsNoMember(orderInfo.get("telephone"));
        if (null == member) {
            member = new Member();
            //不是会员,就跟会员表插入一条数据,操作的是会员表,程序执行的时候当前天,就是执行预约的日期
            member.setRegTime(new Date());
            member.setIdCard(orderInfo.get("idCard"));
            member.setName(orderInfo.get("name"));
            member.setSex(orderInfo.get("sex"));
            member.setPhoneNumber(orderInfo.get("telephone"));
            /*
             * 这里需要键入member的id,后面需要,
             * */
            System.out.println(member);
            memberDao.InsertRemeber(member);

        }
        //会员表已经插入,
        //4 通过订单的日期和会员编号判断是否重复预约,查询的是订单表
        Order order = new Order();
        order.setMemberId(member.getId());
        /**/
        try {
            order.setOrderDate(DateUtils.parseString2Date(orderInfo.get("orderDate")));
        } catch (Exception e) {
            e.printStackTrace();
            throw new HealthException(MessageConstant.ORDER_FAIL);
        }
        //查询的是订单表
        List<Order> list = orderDao.findOderSetting(order);
        if (list != null && list.size() > 0) {
            // 已经预约,也报错
            throw new HealthException(MessageConstant.HAS_ORDERED);
        }

        //设置订单的预约类型
        order.setOrderType(orderInfo.get("orderType"));
        //设置到诊状态为未到诊
        order.setOrderStatus(MessageConstant.UNFILLED_ORDER);
        //设置该订单预约的套餐的id
        order.setPackageId(Integer.valueOf(orderInfo.get("packageId")));
        //执行订单插入
        System.out.println(order);
        orderDao.AddOrder(order);
        //6 预约成功,修改已经预约的人数,操作的订单管理的表,预约成功了,那么那一天的预约人数当然需要加1;
        orderSettingDao.updateOrderNumber(1, orderInfo.get("orderDate"));
        //7 返回值,是订单的id

        Integer id = order.getId();
        return id;
    }

    /*
     *
     * 预约成功,
     * 展示订单详情;
     * 展示套餐注意事项;

     *
     * */
    @Override
    public Map<String, Object> findByIdOrder(Integer id) {
        Map<String, Object> mapOrder = orderDao.findOrderByid(id);

        //拿到会员名,和套餐名;
        Integer packageId = (Integer) mapOrder.get("package_id");
        Integer memberId = (Integer) mapOrder.get("member_id");
        String pkgName = packageDao.findNameByid(packageId);
        String memberName = memberDao.findMemberName(memberId);

        mapOrder.put("pkgName", pkgName);
        mapOrder.put("memberName", memberName);

        return mapOrder;
    }
}
