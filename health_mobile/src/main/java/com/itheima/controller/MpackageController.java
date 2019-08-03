package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Package;

import com.itheima.service.PackageService;
import com.itheima.util.QiNiuUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/package")
public class MpackageController {


    @Reference
    private PackageService packageService;


    /*
     * 查询所有的套餐进行展示;
     * 使用package的接口就可以
     * */
    @RequestMapping("/getPackage")
    public Result getPackage() {

        Result result = new Result();
        try {
            List<Package> list = packageService.findAll();
            /*t_packag表存的package里面存的是图片的名称,没有路径,而我们传到前端的,必须是路径在可以*/
            list.forEach((pkg) -> {
                pkg.setSrc(QiNiuUtil.DOMAIN + "/" + pkg.getSrc());
            });
            result.setFlag(true);
            result.setMessage(MessageConstant.QUERY_SETMEALLIST_SUCCESS);
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setMessage(MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
        return result;
    }


    /*
     * 用户点击套餐,跳转套餐详情页面,参数套餐的id
     * 查询的id的套餐,在页面展示,
     * 返回值:package
     *
     * */
    @RequestMapping("/findById")
    public Result findById(Integer id) {

        try {
            Package pkg = packageService.findById(id);

            //设置图片路径
            pkg.setSrc(QiNiuUtil.DOMAIN+"/"+pkg.getSrc());

            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, pkg);
        } catch (Exception e) {

            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);

        }


    }


    //预约页面加载就发送请求,加载加载套餐的基本信息;

    @PostMapping("/findOnePackageById")
    public Result findById(int id){

        //调用服务,根据id查询套餐信息;
        try {
            Package pkg= packageService.InfoPackageByid(id);
            pkg.setSrc(QiNiuUtil.DOMAIN+"/"+pkg.getSrc());
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, pkg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
    }


}
