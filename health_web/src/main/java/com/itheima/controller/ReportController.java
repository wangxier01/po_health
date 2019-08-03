package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.PackageService;
import com.itheima.service.ReportService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.transform.poi.PoiContext;
import org.jxls.util.JxlsHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/report")
@RestController
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private PackageService packageService;

    @Reference
    private ReportService reportService;

    /*
     * 按月统计会员的总数量;
     * 参数是什么?从哪里来呢?到目前为止一年的
     * */

    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {

        Map<String, Object> memberMap = null;
        try {
            memberMap = memberService.getCountMember();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, memberMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
    }

    /*
     * 参数:
     * 套餐占比统计
     * */

    @GetMapping("/getPackageReport")
    public Result getPackageReport() {
        //一个是套餐name的集合,list
        //一个是套餐name+ 套餐预约次数集合,list<Map<string,object>>
        //返回值有两个,就是有map<string,object>,将两个集合存放进去

        //1 一次查询拿到套餐名字,和套餐的预约次数的集合;
        ArrayList<Map<String, Object>> list = null;
        try {
            list = packageService.findPackageCount();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }

        HashMap<String, Object> map = new HashMap();
        map.put("packageCount", list);
        //2 循环拿到每一个套餐的名字,存放在集合,名字对应的是list的key;
        ArrayList<String> packageNames = new ArrayList();
        for (Map<String, Object> ms : list) {

            String name = (String) ms.get("name");
            packageNames.add(name);
        }
        map.put("packageNames", packageNames);


        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
    }


    /*
     *
     * 运营数据统计分析
     * */
    @PostMapping("/getBusinessReportData")
    public Result getBusinessReportData() {

        //返回值为一个map<string,object>
        try {
            Map<String, Object> map = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);

    }

    //导出运营数据excel;
    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //1 调用远程服务,拿到运营报表数据;
        Map<String, Object> reportData = reportService.getBusinessReportData();
        //2 取出数据,写入到准备好的模板ecxel中去;
        //获取模板的绝对路径,在项目的运行路径里找
        String templatPath = request.getSession().getServletContext().getRealPath("/template/report_template.xlsx");

        //文件名有中文
        String filename = "运营统计数据.xlsx";
        try (
                //使用输出流,进行下载;
                ServletOutputStream os = response.getOutputStream();
                //拿到相应的表单项;
                XSSFWorkbook workbook = new XSSFWorkbook(templatPath);
        ) {
            filename = new String(filename.getBytes(), "ISO-8859-1");

            //设置http的内容体的格式
            response.setContentType("application/vnd.ms-excel");
            //文件名没有中文就这样写
            // response.setHeader("content-Disposition", "attachment;filename=report.xlsx");

            //设置http头的信息;
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);


            //取出数据;
            String reportDay = (String) reportData.get("reportDate");
            Integer todayNewMember = (Integer) reportData.get("todayNewMember");
            Integer totalMember = (Integer) reportData.get("totalMember");
            Integer thisWeekNewMember = (Integer) reportData.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) reportData.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) reportData.get("todayOrderNumber");
            Integer todayVisitsNumber = (Integer) reportData.get("todayVisitsNumber");
            Integer thisWeekOrderNumber = (Integer) reportData.get("thisWeekOrderNumber");
            Integer thisWeekVisitsNumber = (Integer) reportData.get("thisWeekVisitsNumber");
            Integer thisMonthOrderNumber = (Integer) reportData.get("thisMonthOrderNumber");
            Integer thisMonthVisitsNumber = (Integer) reportData.get("thisMonthVisitsNumber");
            List<Map> hotPackage = (List<Map>) reportData.get("hotPackage");

            //3填数据

            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDay); //日期


            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember); //今日新增会员数
            row.getCell(7).setCellValue(totalMember); //会员总数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember); //本周
            row.getCell(7).setCellValue(thisMonthNewMember); //本月

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);
            row.getCell(7).setCellValue(todayVisitsNumber);

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);
            row.getCell(7).setCellValue(thisWeekVisitsNumber);

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);
            row.getCell(7).setCellValue(thisMonthVisitsNumber);


            //热门套餐的问题,
            int rowNum = 12;
            for (Map map : hotPackage) {

                String name = (String) map.get("name");
                Long count = (Long) map.get("count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                String remark = (String) map.get("remark");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);
                row.getCell(5).setCellValue(count);
                row.getCell(6).setCellValue(proportion.doubleValue());
                row.getCell(7).setCellValue(remark);
            }


            workbook.write(os);
            os.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //使用另外一种方式将这个数据写在excel里面实现下载;

    //导出运营数据excel;
    @GetMapping("/exportBusinessReport2")
    public void exportBusinessReport2(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //1 调用远程服务,拿到运营报表数据;
        Map<String, Object> reportData = reportService.getBusinessReportData();
        //2 取出数据,写入到准备好的模板ecxel中去;
        //获取模板的绝对路径,在项目的运行路径里找
        String templatPath = request.getSession().getServletContext().getRealPath("/template/report_template2.xlsx");

        //文件名有中文
        String filename = "运营统计数据.xlsx";
        filename = new String(filename.getBytes(), "ISO-8859-1");

        //设置http的内容体的格式
        response.setContentType("application/vnd.ms-excel");
        //文件名没有中文就这样写
        // response.setHeader("content-Disposition", "attachment;filename=report.xlsx");

        //设置http头的信息;
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        try (
                //模板文件输入流
                FileInputStream is = new FileInputStream(new File(templatPath));
                //输出流;
                ServletOutputStream os = response.getOutputStream();
        ) {

            PoiContext poiContext = new PoiContext();
            poiContext.putVar("obj", reportData);
            JxlsHelper.getInstance().processTemplate(is, os,poiContext);

            os.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
