package com.itheima.springsecurity;
import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;

@Component("springSecurityUserService")
public class SpringSecurityUserService implements UserDetailsService {


    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //1 调用业务根据用户名查询用户,确认登录的是谁
       User user = userService.finduserByName(username);


        //System.out.println(user);
        if (user == null) {

            return null;
        }
        //2 对user进行授权,确定你都有那些权限

        ArrayList<GrantedAuthority> list = new ArrayList();
        //拿到用户的所有角色集合
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //拿到每一个角色的所有权限
            //授权角色,要进行角色认证,你居然忘了;
            SimpleGrantedAuthority roleGrants = new SimpleGrantedAuthority(role.getKeyword());
            list.add(roleGrants);
            Set<Permission> permissions = role.getPermissions();

            for (Permission permission : permissions) {

                //每一种权限,都对应了一些关键字,我们通过这些关键字创建权限对象
                //为什么要添加权限,只有把所有权限都加进去,才能进行权限认证
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }


        }

        //注意这配置了加密器的,必须去掉那个{noop}不处理的标记

        org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
        return userDetail;
    }
}
