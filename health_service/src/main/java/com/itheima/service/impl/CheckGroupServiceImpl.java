package com.itheima.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import com.itheima.service.CheckGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    //添加检查组
    @Override
    @Transactional  //使用数据库的默认隔离级别
    public void addgroup(Integer[] idarr, CheckGroup checkGroup) throws Exception {

        //1 实现增加
        if (checkGroup!=null&& checkGroup.getName()!=null&& checkGroup.getCode()!=null) {

            //通过执行sql语句,给这个对象,键入一个id,原来为null,这是mybatis特有的
        checkGroupDao.addgroup(checkGroup);

        addLinkItem(idarr, checkGroup.getId());

        }else {
            throw new RuntimeException();
        }
    }

    //添加检查组和检查项的关联表
    public void addLinkItem(Integer[] idarr,Integer GId) throws Exception {

        if (idarr!=null && idarr.length>0) {
            for (Integer id : idarr) {

                HashMap<String,Integer> hm = new HashMap();
                //遍历得到每一个被选中的检查项的id
                hm.put("checkgroup_id", GId);
                hm.put("checkitem_id", id);

                try {
                    checkGroupDao.addItemAndGroup(hm);
                } catch (Exception e) {
                    throw new Exception();
                }
            }

        }else {
            throw new Exception();
        }
    }

    /*
    *
    * 完成检查组的分页
    *使用mabtis的分页插件
    * */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {

            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        //使用分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

       Page<CheckGroup> page= checkGroupDao.findAllBycondition(queryPageBean.getQueryString());

       PageResult pageResult = new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }

    /*
    *
    * 根据id查询,检查组
    * */
    @Override
    public CheckGroup findGroupItem(Integer groupId) {
        return checkGroupDao.findGroup(groupId);
    }

    /*
    * 根据检查组的id,查询所包含的检查项
    * */
    @Override
    public List<Integer> findGroupItems(Integer id) {

        return checkGroupDao.findGroupItems(id);
    }

    //更新检查组,点击提交之后要修改表,以及检查组与检查项的中间表
    @Override
    @Transactional
    public void updatGroupAndLink(Integer[] checkitemIds, CheckGroup checkGroup) {

        //拿到检查组的id
        Integer checkgroupId = checkGroup.getId();


        //调用dao,更新检查组的基本信息检查组
        checkGroupDao.updateGroup(checkGroup);

        //根据groupid,删除之前的关联;
        checkGroupDao.deleteByGroupId(checkgroupId);

        if (checkitemIds!=null && checkitemIds.length>0) {
            //遍历数组
            for (Integer checkitemId : checkitemIds) {

                checkGroupDao.addLinkItemAndGroup(checkgroupId,checkitemId);

            }

        }






    }
/*
* 查询所有检查组信息,展示在新建套餐表单
* */
    @Override
    public List<CheckGroup> findAll() {


        return checkGroupDao.findAll();
    }


}
