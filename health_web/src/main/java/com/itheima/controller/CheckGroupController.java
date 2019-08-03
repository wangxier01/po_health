package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    //提交新增检查组的请求了,参数是:检查组对象和所选择的检查项的id集合
    @PostMapping("/addgroup")
    public Result addgroup(Integer[] checkitemIds, @RequestBody CheckGroup formData) {
        Result result = new Result(true);

        try {
            checkGroupService.addgroup(checkitemIds, formData);
            result.setMessage(MessageConstant.ADD_CHECKGROUP_SUCCESS);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setMessage(MessageConstant.ADD_CHECKGROUP_FAIL);

            return result;
        }
    }

    //分页
    @RequestMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {

        //System.out.println(queryPageBean);

        Result result = new Result(true);
        try {
            PageResult page = checkGroupService.findPage(queryPageBean);
            result.setData(page);
            result.setMessage(MessageConstant.QUERY_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage(MessageConstant.QUERY_CHECKGROUP_FAIL);
            result.setFlag(false);
        }
        return result;
    }


    /*
     * 编辑检查组,根据id查询检查组
     * */

    @PostMapping("/findGroup")
    public Result findGroupItem(Integer GroupId) {
        Result result = new Result(true);
        try {
            //要编辑一个检查组,根据id查询到的是一个检查组对象
            CheckGroup checkGroup = checkGroupService.findGroupItem(GroupId);
            result.setMessage(MessageConstant.QUERY_CHECKGROUP_SUCCESS);
            result.setData(checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setMessage(MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
        return result;
    }

    //查询检查组的检查项,让他选中,参数是一个数组
    @PostMapping("/findGroupItem")
    public Result findGroupliskItem(@Param("groupId") Integer groupId) {
        //一个检查组多个检查项;
        try {
            List<Integer> checkGroups = checkGroupService.findGroupItems(groupId);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkGroups);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }

    //增加检查组
    @PostMapping("/addgroupLink")
    public Result updategroupAndLink(Integer[] checkitemIds, @RequestBody CheckGroup checkGroup) {

        //System.out.println(checkitemIds+""+checkGroup);

        try {
            checkGroupService.updatGroupAndLink(checkitemIds, checkGroup);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
    }

    //查询所有的检查组,展示在新增套餐表单
    @PostMapping("/findAll")
    public Result findAll() {

        try {
            List<CheckGroup> list = checkGroupService.findAll();

            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }



}
