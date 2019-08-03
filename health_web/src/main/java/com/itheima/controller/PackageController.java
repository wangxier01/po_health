package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Package;
import com.itheima.service.PackageService;
import com.itheima.util.QiNiuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import javax.ws.rs.GET;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/package")
public class PackageController {

    @Reference
    private PackageService packageService;

    /*注入jedis连接池*/
    @Autowired
    private JedisPool jedisPool;


    /*
     * 接收上传图片的请求
     * 参数为imgFile,这个是前端标签里面name属性对应的,这里没有使用redis数据据库
     *
     * */


    //测试一下获取文件的后缀名是否正确
    public static void main(String[] args) {
        String originalFilename = "1.jpg";
        //2 获取文件的后缀名,把这个点给留着,因为后面拼接文件名的时候,也要加点
        String extensionName = originalFilename.substring(originalFilename.lastIndexOf("."));
        System.out.println(extensionName);
    }

    //主页分页展示
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {

        try {
            PageResult pageResult = packageService.findPage(queryPageBean);

            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, pageResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
    }

    /*
     * 完成添加检查套餐的请求
     *
     * */

    @PostMapping("/addpackage")
    public Result addpackage(@RequestBody Package pkg, Integer[] checkgroupIds) {

        /*
        * 传传转数子,乘以1
        * */
        try {
            packageService.addpackage(pkg, checkgroupIds);

            //添加套餐成功之后,将图片存到redis的db,SETMEAL_PIC_DB_RESOURCES
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pkg.getSrc());

            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
    }


    // 使用redis,改进图片上传的


    @RequestMapping("/upload2")
    public Result upload2(@RequestParam("imgFile") MultipartFile imgFile) {

        //1 获取文件的后缀名,源文件的后缀名为xxx.jpg
        String filename = getFileName(imgFile);
        //4 调用七牛的工具,把图片上传给七牛,产生唯一的文件名,不能重复,然后将这个唯一的文件名返回给前端,在七牛中的domain也要一起返回给前端
        try {
            HashMap<String, String> imgMap = new HashMap();
            //4.1 回传七牛的图片域名
            imgMap.put("domain", QiNiuUtil.DOMAIN);
            //4.2 我们上传到七牛的文件在七牛的那个文件名称,就是我们刚刚造的那个唯一的
            //也是以后存在数据库的文件名
            imgMap.put("imgName", filename);

            QiNiuUtil.uploadViaByte(imgFile.getBytes(), filename);

            /*
             * 上传成功,将图片名称保存在redis,基于redis的set集合存储,和他进行加减
             * 在添加成功的时候,在存一次
             * */
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, filename);
            //上传成功
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, imgMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //上传失败
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }

    /*抽取方法,获取文件名*/
    public String getFileName(MultipartFile imgFile) {
        String originalFilename = imgFile.getOriginalFilename();
        //2 获取文件的后缀名,把这个点给留着,因为后面拼接文件名的时候,也要加点
        String extensionName = originalFilename.substring(originalFilename.lastIndexOf("."));

        //3 产生一个唯一的文件名称
        UUID uuid = UUID.randomUUID();
        //文件名有了,唯一的数据拼接后缀
        return uuid + extensionName;
    }



     /* @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {

        //1 获取文件的后缀名,源文件的后缀名为xxx.jpg
        String filename = getFileName(imgFile);
        //4 调用七牛的工具,把图片上传给七牛,产生唯一的文件名,不能重复,
        // 然后将这个唯一的文件名返回给前端,在七牛中的domain也要一起返回给前端
        try {
            HashMap<String, String> imgMap = new HashMap();
            //4.1 回传七牛的图片域名

             // 这个东面,也就是七牛给我们的域名,不能在页面中写死,万一七牛变了,修改起来就很麻烦
             imgMap.put("domain", QiNiuUtil.DOMAIN);
            //4.2 我们上传到七牛的文件在七牛的那个文件名称,就是我们刚刚造的那个唯一的
            //也是以后存在数据库的文件名
            imgMap.put("imgName", filename);

            QiNiuUtil.uploadViaByte(imgFile.getBytes(), filename);

            //上传成功
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, imgMap);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //上传失败
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }
*/


    //执行删除套餐
    @GetMapping("/deletePackage")
    public Result deletePackage(Integer packageId) {

        try {
            packageService.deletePackage(packageId);
            return new Result(true, "删除套餐成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "删除套餐失败");
    }


    //编辑套餐,实现回显根基id
    @RequestMapping("/findPackageById")
    public Result findPackageById(@RequestParam("id") Integer id) {

        Result result = new Result(true);
        try {
            Package pkg = packageService.findPackageById(id);
            result.setMessage(MessageConstant.QUERY_SETMEAL_SUCCESS);
            result.setData(pkg);
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setMessage(MessageConstant.QUERY_SETMEAL_FAIL);
        }

        return result;
    }
    //查询套餐,都选择了那些检查组进行回显
    @PostMapping("/findPackageGroup")
    public Result findPackageGroup(@RequestParam("id") Integer id){
        Result result = new Result(true);
        try {
            List<Integer> ids= packageService.findPackageGroup(id);
            result.setData(ids);

        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
        }

        return result;
    }


    /*
    *
    * 执行套餐的修改
    * 参数:一个package,一个checkgroup数组
    * */

    @PostMapping("/handleEdit")
    public Result handleEdit( @RequestParam("checkgroupIds") Integer[] checkgroupIds,@RequestBody Package pkg){

        Result result = new Result(true);
        try {
            packageService.handleEdit(pkg,checkgroupIds);
            result.setMessage("修改套餐成功");
        } catch (Exception e) {
            result.setMessage("修改套餐失败");
            result.setFlag(false);
        }

        return result;
    }

}
