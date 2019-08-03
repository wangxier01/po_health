package com.itheima.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    //业务层调用dao
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    //分页
    @Override
    public PageResult findpage(QueryPageBean queryPageBean) {

        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            //查询条件不为空,就拼接%进行模糊查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //框架里面的分页方法:

        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //传参数:模糊查询的条件给dao,执行sql,返回一个page对象,这个page对象是框架里面的,包含了分页的所有的数据;

        Page<CheckItem> page = checkItemDao.findAllBycondition(queryPageBean.getQueryString());



        //框架方法里面取到当前页的总记录数,和当前页集合,总记录数,和当前页集合

        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());

        return pageResult;
    }

    //删除检查项
    @Override
    @Transactional
    public void delete(Integer id) {

        //判读昂当前id对应的检查主是否被检查组引用;
       Integer count= checkItemDao.cheeckById(id);

        if (count>0) {
          //已经被引用
            throw new RuntimeException("当前检查项已经被检查组引用,不能执行删除");

        }else {
          //未被引用
            checkItemDao.deleteById(id);

        }





    }

    //查询数据,完成数据回显
    @Override
    public CheckItem findOneById(Integer id) {


        return checkItemDao.finOneById(id);
    }

    //修改检查项
    @Override
    public void updateOne(CheckItem checkItem) {


        checkItemDao.updateOne(checkItem);

    }

    //在新建检查组的弹窗里面,需要展示所有的检查项
    @Override
    public List<CheckItem> findAllShow() {

        return checkItemDao.findAllShow();
    }

}
