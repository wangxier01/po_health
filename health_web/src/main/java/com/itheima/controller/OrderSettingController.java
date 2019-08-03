package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.util.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("/ordersetting")
@RestController
public class OrderSettingController {

    /*订阅服务*/
    @Reference
    private OrderSettingService orderSettingService;

    /*
    *
    * 批量上传预约信息
    * */
    @RequestMapping("/upload")
    public Result upLoad(@RequestParam("excelFile")MultipartFile excelFile){
        //传过来的参数是一个流对象;excelFile只是一个变量名


        //读取excel的数据
        try {
            //list里面装的是数组,每一个数组,表示一行;0是第一行
            List<String[]> list = POIUtils.readExcel(excelFile);
            if (list != null && list.size() >0) {
                ArrayList<OrderSetting> ordersettingList = new ArrayList();

                //遍历集合拿到每一行
                for (String[] strings : list) {
                    //每个一行的第一个单元格是日期,第二个单元格是数量
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));

                    //使用单元格的数据,创建实体类对象,将实体类对象存放在集合
                    ordersettingList.add(orderSetting);
                }

                //调用远程服务,进行判断处理,数据上传到数据库;
                orderSettingService.add(ordersettingList);

                return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
    }



    /*
    *
    * 日历格式,按月展示预约信息,
    * 可预约的人数,已经预约的人数,
    * 参数:某年某月
    * 返回:List<map>或者List<ordersetting>,推荐使用map
    *
    * */

    @RequestMapping("/findPredietMonth")
    public Result findPredietMonth(String date){

        try {
            List<Map> list=  orderSettingService.findOrdersettingByMonth(date);
            System.out.println(list);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
    }


    /*
    *
    * 设置可预约人数
    * 参数:日期,人数
    *
    *
    * */
    @PostMapping("/setNumPerson")
    public  Result setNumPerson(@RequestBody OrderSetting orderSetting) {

      // SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        /*
        * 前端传递参数过来json对象,服务器用javabean接收,封装不上的,如果是放在post请求体里面,就可以使用,pramas{},这种格式;
        * 服务器使用@requestBody进行封装;
        * 也可以使用map,键值对进行封装;也可以使用,单个进行接收.
        *
        *
        *
        *
        * */

        try {
            orderSettingService.setNumPerson(orderSetting);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
    }
}
