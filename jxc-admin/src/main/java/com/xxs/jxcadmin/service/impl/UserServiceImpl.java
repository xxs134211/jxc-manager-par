package com.xxs.jxcadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxs.jxcadmin.exceptions.ParamsException;
import com.xxs.jxcadmin.mapper.UserMapper;
import com.xxs.jxcadmin.pojo.User;
import com.xxs.jxcadmin.pojo.UserRole;
import com.xxs.jxcadmin.query.UserQuery;
import com.xxs.jxcadmin.service.IUserRoleService;
import com.xxs.jxcadmin.service.IUserService;
import com.xxs.jxcadmin.utils.AssertUtil;
import com.xxs.jxcadmin.utils.PageResultUtil;
import com.xxs.jxcadmin.utils.StringUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IUserRoleService userRoleService;

//    @Override
//    public User login(String userName, String password) {
//        AssertUtil.isTrue(StringUtil.isEmpty(userName), "用户名不能为空");
//        AssertUtil.isTrue(StringUtil.isEmpty(password), "密码不能为空");
//        User user = this.findUserByUsername(userName);
//        AssertUtil.isTrue(null == user, "该用户不存在或者已注销!");
//        if(user != null){
//            AssertUtil.isTrue(!(user.getPassword().equals(password)),"密码错误！");
//        }else{
//            throw  new ParamsException("用户名不能为空!");
//        }
//        return user;
//    }

    @Override
    public User findUserByUsername(String userName) {
        return this.baseMapper.selectOne(new QueryWrapper<User>().eq("is_del", 0).eq("user_name", userName));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateUserInfo(User user) {
        AssertUtil.isTrue(StringUtil.isEmpty(user.getUsername()),"用户名不能为空！");
        User temp = this.findUserByUsername(user.getUsername());
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
        AssertUtil.isTrue(!(passwordEncoder.matches(oldPassword,user.getPassword())),"原始密码输入不正确！");
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)),"新密码输入不一致！");
        AssertUtil.isTrue(newPassword.equals(oldPassword),"新密码与原始密码不能一致！");
        user.setPassword(passwordEncoder.encode(newPassword));
        AssertUtil.isTrue(!(this.updateById(user)),"用户密码更新失败！");
    }

    @Override
    public Map<String, Object> userList(UserQuery userQuery) {
        IPage<User> page = new Page<>(userQuery.getPage(),userQuery.getLimit());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del",0);
        if(StringUtils.isNotBlank(userQuery.getUserName())){
            queryWrapper.like("user_name",userQuery.getUserName());
        }
        page = this.baseMapper.selectPage(page, queryWrapper);

        return PageResultUtil.getResult(page.getTotal(),page.getRecords());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveUser(User user) {
        AssertUtil.isTrue(StringUtils.isBlank(user.getUsername()),"用户名不能为空！");
        AssertUtil.isTrue(null != this.findUserByUsername(user.getUsername()),"用户名已存在！");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setIsDel(0);
        AssertUtil.isTrue(!this.save(user),"用户添加失败！");

        User temp = this.findUserByUsername(user.getUsername());

        relationUserRole(temp.getId(),user.getRoleIds());
    }

    private void relationUserRole(Integer userId, String roleIds) {

        int count = userRoleService.count(new QueryWrapper<UserRole>().eq("user_id",userId));
        if(count>0){
            AssertUtil.isTrue(!(userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id",userId))),"用户角色分配失败！");
        }
        if(StringUtils.isNotBlank(roleIds)){
            List<UserRole> userRoles = new ArrayList<>();
            for (String s : roleIds.split(",")) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(Integer.parseInt(s));
                userRoles.add(userRole);
            }
            AssertUtil.isTrue(!userRoleService.saveBatch(userRoles),"用户角色分配失败！");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateUser(User user) {
        AssertUtil.isTrue(StringUtils.isBlank(user.getUsername()),"用户名不能为空！");
        User temp = this.findUserByUsername(user.getUsername());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(user.getId())),"用户名已存在！");
        relationUserRole(user.getId(),user.getRoleIds());
        AssertUtil.isTrue(!this.updateById(user),"用户更新失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteUser(Integer[] ids) {
        AssertUtil.isTrue(null == ids || ids.length==0,"请选择要删除的记录！");
        assert ids != null;
        int count = userRoleService.count(new QueryWrapper<UserRole>().in("user_id", Arrays.asList(ids)));
        if(count>0){
            AssertUtil.isTrue(!(userRoleService.remove(new QueryWrapper<UserRole>().in("user_id",Arrays.asList(ids)))),"用户角色记录删除失败！");
        }
        List<User> users = new ArrayList<>();
        for (Integer id: ids) {
            User temp = this.getById(id);
            temp.setIsDel(1);
            users.add(temp);
        }
        AssertUtil.isTrue(!(this.updateBatchById(users)),"删除成功！");
    }
}











