package com.xxs.jxcadmin.controller;


import com.xxs.jxcadmin.exceptions.ParamsException;
import com.xxs.jxcadmin.model.RespBean;
import com.xxs.jxcadmin.pojo.User;
import com.xxs.jxcadmin.service.IUserService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author xxs
 * @since 2021-05-06
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @RequestMapping("login")
    @ResponseBody
    public RespBean login(String userName, String password, HttpSession session){
            User user = userService.login(userName,password);
            session.setAttribute("user",user);
            return RespBean.success("用户登录成功！");
    }

    @RequestMapping("setting")
    public String setting(HttpSession session){
        User user = (User) session.getAttribute("user");
        session.setAttribute("user",userService.getById(user.getId()));
        return "user/setting";
    }

    @RequestMapping("updateUserInfo")
    @ResponseBody
    public RespBean updateUserInfo(User user){
            userService.updateUserInfo(user);
            return RespBean.success("用户信息更新成功");
    }

    @RequestMapping("password")
    public String password(){
        return "user/password";
    }

    @RequestMapping("updateUserPassword")
    @ResponseBody
    public RespBean updateUserPassword(HttpSession session, String oldPassword, String newPassword, String confirmPassword){
            User user = (User) session.getAttribute("user");
            userService.updateUserPassword(user.getUserName(),oldPassword,newPassword,confirmPassword);
            return RespBean.success("用户密码更新成功");
    }
}
