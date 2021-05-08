package com.xxs.jxcadmin.controller;

import com.xxs.jxcadmin.model.RespBean;
import com.xxs.jxcadmin.pojo.Role;
import com.xxs.jxcadmin.query.RoleQuery;
import com.xxs.jxcadmin.service.IRoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 13421
 */
@Controller
@RequestMapping("/role")
public class RoleController {
    @Resource
    private IRoleService roleService;

    /**
     * 角色管理主页
     * @return 角色主页面
     */
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    /**
     * 角色列表查询
     * @param roleQuery 角色对象
     * @return 角色的List集合
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> roleList(RoleQuery roleQuery){
        return roleService.roleList(roleQuery);
    }

    @RequestMapping("addOrUpdateRolePage")
    public String addOrUpdatePage(Integer id, Model model){
        if(null != id){
            model.addAttribute("role",roleService.getById(id));
        }
        return "role/add_update";
    }

    @RequestMapping("save")
    @ResponseBody
    public RespBean saveUser(Role role){
        roleService.saveRole(role);
        return RespBean.success("角色添加成功！");
    }
    @RequestMapping("update")
    @ResponseBody
    public RespBean updateRole(Role role){
        roleService.updateRole(role);
        return RespBean.success("角色更新成功！");
    }

    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteRole(Integer id){
        roleService.deleteRole(id);
        return RespBean.success("删除成功！");
    }

    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.queryAllRoles(userId);
    }
}
