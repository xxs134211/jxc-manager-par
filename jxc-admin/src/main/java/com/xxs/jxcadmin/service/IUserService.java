package com.xxs.jxcadmin.service;

import com.xxs.jxcadmin.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author xxs
 * @since 2021-05-06
 */
public interface IUserService extends IService<User> {

    /**
     * 登录注册
     * @param userName 用户名
     * @param password 密码
     * @return 登录是否成功
     */
    User login(String userName, String password);

    public User findUserByUsername(String userName);

    public void updateUserInfo(User user);

    void updateUserPassword(String userName, String oldPassword, String newPassword, String confirmPassword);
}
