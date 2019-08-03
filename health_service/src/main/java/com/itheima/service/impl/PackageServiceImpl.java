package com.itheima.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.PackageDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Package;
import com.itheima.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*使用dubbo将其放置核心容器*/
@Service(interfaceClass =PackageService.class)
public class PackageServiceImpl implements PackageService {

    /*注入dao的代理对象*/
    @Autowired
    private PackageDao packageDao;

    /*
     * 套餐分页展示
     * */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //1 拿到分页查询的条件
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
            //不为空就给拼接%,执行就按条件查询
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");

        }
        //2 调用mybatis的工具分页
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //3 调用dao,将查询到的数据封装在page里面;
        Page<Package> page = packageDao.findPage(queryPageBean.getQueryString());
        //4 创建pageresult
        PageResult pageResult = new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }
    /*
    * 添加套餐,
    * 维护关系表
    * */
    @Override
    @Transactional
    public void addpackage(Package pkg, Integer[] checkgroupIds) throws Exception {

        if (pkg.getName()!=null && pkg.getCode() != null){
        //1 执行添加;
        packageDao.addpackage(pkg);
        //2 获取套餐的id
        Integer pkgId = pkg.getId();
        //3 维护关系表
            if (checkgroupIds != null && checkgroupIds.length >0) {

                for (Integer checkgroupId : checkgroupIds) {

                    HashMap<String,Integer> hm = new HashMap();
                    hm.put("package_id", pkgId);
                    hm.put("checkgroup_id", checkgroupId);
                    packageDao.addLinkTable(hm);

                }
            }else {

                throw new RuntimeException();
            }

        }else {

            throw new Exception();
        }
    }

    //删除套餐
    @Override
    @Transactional
    public void deletePackage(Integer groupId) throws Exception {

        //删除套餐,删除关系表里面相关的数据
        if (groupId==null){
            throw new Exception();
        }

        //1 删除关系表的数据

        packageDao.deletePackageAndLink(groupId);

        //2 删除套餐
        packageDao.deletePackage(groupId);
    }

    @Override
    public Package findPackageById(Integer id) {
        return packageDao.findPackageById(id);
    }

    /*
    *
    * 查询套餐的检查组*/
    @Override
    public List<Integer> findPackageGroup(Integer id) {
        List<Integer> list = packageDao.findPackageGroup(id);
        return list;
    }

    /*
    * 修改套餐
    * */
    @Override
    @Transactional
    public void handleEdit(Package pkg, Integer[] checkgroupIds) {

        if (pkg.getName()!=null && pkg.getCode()!=null) {

                //1 根据pkg,去修改;
                packageDao.handleEdit(pkg);
                //2 维护关系表
                Integer package_id = pkg.getId();
                //3 删除

               packageDao.handleEditDelete(package_id);

                //4 添加关系
                if (checkgroupIds.length >0) {

                    for (Integer checkgroup_id : checkgroupIds) {

                        packageDao.handleLinkTable(package_id,checkgroup_id);

                    }
                }
        }




    }

    /*
    *
    * 移动端展示所有套餐
    * */
    @Override
    public List<Package> findAll() {
        return packageDao.findAll();
    }


    /*
    * 根据id查询套餐信息详情,
    * 参数:id
    * 返回值:package
    * 需要:package.name,package.remark,package.sex,package.age
    *      checkgroup.name,remark,
    *      checkitem.name
    *
    * */
    @Override
    public Package findById(Integer id) {


        return packageDao.findById(id);
    }




    /*
    * 移动端,预约页面加载时候展示套餐的基本信息
    *
    *
    * */
    @Override
    public Package InfoPackageByid(int id) {


        return packageDao.InfoPackageById(id);
    }


    /*
    * 套餐预约占比
    * */
    @Override
    public ArrayList<Map<String, Object>> findPackageCount() {



        return packageDao.findPackageCount();
    }

}
