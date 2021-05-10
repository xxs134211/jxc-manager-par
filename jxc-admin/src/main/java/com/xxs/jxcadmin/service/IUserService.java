package com.xxs.jxcadmin.service;

import com.xxs.jxcadmin.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxs.jxcadmin.query.UserQuery;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author xxs
 * @since 2021-05-06
 */
public interface IUserService extends IService<User> {


    public User findUserByUsername(String userName);

    public void updateUserInfo(User user);

    void updateUserPassword(String userName, String oldPassword, String newPassword, String confirmPassword);

    Map<String, Object> userList(UserQuery userQuery);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUser(Integer[] ids);
}
