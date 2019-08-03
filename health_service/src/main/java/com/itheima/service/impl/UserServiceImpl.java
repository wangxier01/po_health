package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;


@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;


    /*根据用户名查询数据的user*/
    @Override
    public User finduserByName(String username) {
        User user = userDao.findUserByName(username);
        if (user != null) {
            //根基用户id查询角色;查询角色表
            Integer userId = user.getId();
            Set<Role> roles = roleDao.findUserRoleByid(userId);

            //根据角色id,查询对应的权限id
            if (roles != null && roles.size() > 0) {

                for (Role role : roles) {
                    Integer roleId = role.getId();
                    Set<Permission> permissions = permissionDao.findpermissionById(roleId);
                    role.setPermissions(permissions);
                }
            }

            user.setRoles(roles);

        }

        return user;
    }
}
