package com.xxs.jxcadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxs.jxcadmin.exceptions.ParamsException;
import com.xxs.jxcadmin.mapper.UserMapper;
import com.xxs.jxcadmin.pojo.User;
import com.xxs.jxcadmin.service.IUserService;
import com.xxs.jxcadmin.utils.AssertUtil;
import com.xxs.jxcadmin.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xxs
 * @since 2021-05-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User login(String userName, String password) {
        AssertUtil.isTrue(StringUtil.isEmpty(userName), "用户名不能为空");
        AssertUtil.isTrue(StringUtil.isEmpty(password), "密码不能为空");
        User user = this.findUserByUsername(userName);
        AssertUtil.isTrue(null == user, "该用户不存在或者已注销!");
        if(user != null){
            AssertUtil.isTrue(!(user.getPassword().equals(password)),"密码错误！");
        }else{
            throw  new ParamsException("用户名不能为空!");
        }
        return user;
    }

    @Override
    public User findUserByUsername(String userName) {
        return this.baseMapper.selectOne(new QueryWrapper<User>().eq("is_del", 0).eq("user_name", userName));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateUserInfo(User user) {
        AssertUtil.isTrue(StringUtil.isEmpty(user.getUserName()),"用户名不能为空！");
        User temp = this.findUserByUsername(user.getUserName());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(user.getId())),"用户名已存在！");
        AssertUtil.isTrue(!(this.updateById(user)),"用户信息更新失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateUserPassword(String userName, String oldPassword, String newPassword, String confirmPassword) {
        User user;
        user = this.findUserByUsername(userName);
        AssertUtil.isTrue(null == this.findUserByUsername(userName), "用户不存在或未登录！");
        AssertUtil.isTrue(StringUtil.isEmpty(oldPassword),"请输入原始密码!");
        AssertUtil.isTrue(StringUtil.isEmpty(newPassword),"请输入新密码!");
        AssertUtil.isTrue(StringUtil.isEmpty(confirmPassword),"请再输入一边确认密码!");
        AssertUtil.isTrue(!(user.getPassword().equals(oldPassword)),"原始密码输入不正确！");
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)),"新密码输入不一致！");
        AssertUtil.isTrue(newPassword.equals(oldPassword),"新密码与原始密码不能一致！");
        user.setPassword(newPassword);
        AssertUtil.isTrue(!(this.updateById(user)),"用户密码更新失败！");
    }
}
