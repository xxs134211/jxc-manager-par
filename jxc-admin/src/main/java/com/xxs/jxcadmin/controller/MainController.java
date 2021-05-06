package com.xxs.jxcadmin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @author 13421
 */
@Controller
public class MainController {

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("main")
    public String main(){
        return "main";
    }
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }
    @RequestMapping("signout")
    public String signout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:index";
    }
}
