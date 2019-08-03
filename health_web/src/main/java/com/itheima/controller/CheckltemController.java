package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.exception.HealthException;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkitem")
public class CheckltemController {

    @Reference
    private CheckItemService checkItemService;

    //添加
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) throws HealthException {

       // System.out.println("添加检查项" + checkItem);

        Result result = new Result(true);

        System.out.println("222");






        try {
            System.out.println(checkItemService);
            checkItemService.add(checkItem);
            result.setMessage(MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setMessage(MessageConstant.ADD_CHECKITEM_FAIL);

        }

        System.out.println(result);
        return result;
    }


    //分页展示
    /*
    *  @PreAuthorize("hasAnyAuthority('CHECKITEM_QUERY')")
    *  数据库插到的用户的权限里面,如果还有方法注解上面的权限,或者角色,那么,他就可以调用这个方法;也就是可以访问
    *   例如:这里配置了一个检查项的查询权限,可以访问的注解,查询的到用户如果有这个权限,就可以访问
    *
    * */
    @RequestMapping("/findpage")
    @PreAuthorize("hasAnyAuthority('CHECKITEM_QUERY')")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {

        //查询所有,分页;
       // System.out.println("分页展示");
        Result result = new Result(true);
        PageResult pageResult = null;
        try {
            pageResult = checkItemService.findpage(queryPageBean);
            System.out.println(pageResult.getTotal());
            result.setData(pageResult);
            result.setMessage(MessageConstant.QUERY_CHECKITEM_SUCCESS);
        } catch (Exception e) {

            e.printStackTrace();
            result.setFlag(false);
            result.setMessage(MessageConstant.DELETE_CHECKITEM_FAIL);
        }

        return result;
    }

    //删除,参数为id
    @RequestMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result delete(Integer id) {
        Result result = new Result(true);
        try {
            //调用服务
            checkItemService.delete(id);
            //结果的提示信息为删除成功
            result.setMessage(MessageConstant.DELETE_CHECKITEM_SUCCESS);

        } catch (RuntimeException e) {
            result.setFlag(false);
            result.setMessage(e.getMessage());
            return result;
        } catch (Exception e2) {
            result.setFlag(false);
            result.setMessage(MessageConstant.DELETE_CHECKITEM_FAIL);
            return result;
        }
        return result;
    }

    //执行数据回显
    @RequestMapping("/findeoneById")
    public Result findeoneById(Integer id){
        System.out.println(id);
        Result result = new Result(true);

        try {
            CheckItem checkItem= checkItemService.findOneById(id);

            result.setData(checkItem);
            result.setMessage(MessageConstant.QUERY_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setMessage(MessageConstant.QUERY_CHECKITEM_FAIL);
        }

        return  result;
    }

    //更新数据
    @PostMapping("/update")
    public Result update(@RequestBody CheckItem checkItem){


        Result result = new Result(true);

        try {
            checkItemService.updateOne(checkItem);
            result.setMessage(MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setMessage(MessageConstant.EDIT_CHECKITEM_FAIL);
        }

        return result;
    }


    //在新建检查组的弹框显示所有的检查项的信息,这个是需要带一个选择框的
    @PostMapping("/findAllShow")
    public  Result findAllShow(){
        Result result = new Result(true);

        try {
            List<CheckItem> list= checkItemService.findAllShow();
            result.setData(list);
            result.setMessage(MessageConstant.QUERY_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setMessage(MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return result;
    }


}
