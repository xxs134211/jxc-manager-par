package com.xxs.jxcadmin.controller;


import com.xxs.jxcadmin.model.RespBean;
import com.xxs.jxcadmin.pojo.Customer;
import com.xxs.jxcadmin.pojo.Role;
import com.xxs.jxcadmin.query.CustomerQuery;
import com.xxs.jxcadmin.service.ICustomerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 客户表 前端控制器
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Resource
    private ICustomerService customerService;
    @RequestMapping("index")
    public String index(){
        return "customer/customer";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> customerList(CustomerQuery customerQuery){
        return customerService.customerList(customerQuery);
    }

    @RequestMapping("addOrUpdateCustomerPage")
    public String addOrUpdatePage(Integer id, Model model){
        if(null != id){
            model.addAttribute("customer",customerService.getById(id));
        }
        return "customer/add_update";
    }
    @RequestMapping("save")
    @ResponseBody
    public RespBean saveCustomer(Customer customer){
        customerService.saveCustomer(customer);
        return RespBean.success("客户添加成功！");
    }
    @RequestMapping("update")
    @ResponseBody
    public RespBean updateCustomer(Customer customer){
        customerService.updateCustomer(customer);
        return RespBean.success("客户信息更新成功！");
    }

    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteCustomer(Integer[] ids){
        customerService.deleteCustomer(ids);
        return RespBean.success("删除成功！");
    }
}
