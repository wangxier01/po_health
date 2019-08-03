package com.itheima.service;

import com.itheima.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

/*
*
* security高级练习
* */

public class springSecurityUserServiceImpl  implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        //1 根据用户名查user,认证,你是谁
       User user =  findUserByName(username);


        if (user==null) {

            //认证失败
            return  null;
        }
        //2 授权
        /*
        * GrantedAuthority
        * 查询发现是一个接口,那马这个时候,我们需要找找他的实现类
        *
        * */
        ArrayList<GrantedAuthority> list = new ArrayList();
        list.add(new SimpleGrantedAuthority("add"));
        list.add(new SimpleGrantedAuthority("delete"));
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        list.add(new SimpleGrantedAuthority("update"));
        /*
        * UserDetails是一个接口,他有一个实现类是user
        * 使用明文,不加密码,就需要写上这个明文标识{noop}
        *
        * */

        org.springframework.security.core.userdetails.User userDetials = new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),list);
        return userDetials;
    }

    /*模拟查询*/
    private User findUserByName(String username) {

        if ("admin".equals(username)) {

            User user = new User();
            user.setUsername("admin");
            //对1234机密的密文
            user.setPassword("$2a$10$NF48KFABUFd/Cj5fDABzLO34YMydanve6.nI6.x/.5J5T390R51Ia");

            return user;
        }
        return null;
    }
}
