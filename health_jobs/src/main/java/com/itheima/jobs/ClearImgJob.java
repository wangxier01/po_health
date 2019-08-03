package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.util.QiNiuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/*
 * 清除图片的任务
 *
 *
 * */
@Component
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {

        // 计算两个set的差值,记住:第一个是多的,是上传的位置,第二个是成功后保存的位置;
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //2 遍历集合,拿到每一个差值图片的名称
        // clearMethod1(set);
        //3 推荐使用第二种,将七牛的垃圾图片清除和redis里存放的图片,也清除
        clearMethod2(set);

    }

    //方法1 ,遍历差集删除
    public void clearMethod1(Set<String> set) {

        for (String pic : set) {
            //3 删除七牛空间上的
            QiNiuUtil.removeFiles(pic);
            //4 删除redis里面的
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, pic);

        }

    }

    public void clearMethod2(Set<String> set) {

        //1 将需要删除的set集合,转换成字符串数组;
        String[] needDeleteImg2 = set.toArray(new String[]{});

        //2 调用七牛批量删除,这个方法可以接收单个图片,也可以接收一个可变数组,所以
        QiNiuUtil.removeFiles(needDeleteImg2);

        //3 redis里面存放上传成功的所有的图片集合也必须删掉,以及登录成功上传的图片;
        Jedis jedis = jedisPool.getResource();
        //下面方法是只删除差集
        //jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES, needDeleteImg2);

        //下面方法,是全部删除,因为一旦调用一次之后,多余的图片,都已经删了,再存着也没有用了
        jedis.del(RedisConstant.SETMEAL_PIC_RESOURCES,RedisConstant.SETMEAL_PIC_DB_RESOURCES);


    }


}
