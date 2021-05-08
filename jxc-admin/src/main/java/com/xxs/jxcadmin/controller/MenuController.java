package com.xxs.jxcadmin.controller;

import com.xxs.jxcadmin.dto.TreeDto;
import com.xxs.jxcadmin.model.RespBean;
import com.xxs.jxcadmin.pojo.Menu;
import com.xxs.jxcadmin.service.IMenuService;
import com.xxs.jxcadmin.service.IRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author 13421
 */
@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @RequestMapping("index")
    public String index(){
        return "menu/menu";
    }
    /**
     * 返回所有菜单的数据
     * @return
     */
    @RequestMapping("queryAllMenus")
    @ResponseBody
    public List<TreeDto> quertAllMenus(Integer roleId){
        return menuService.queryAllMenus(roleId);
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> menuList(){
        return menuService.menuList();
    }

    @RequestMapping("addMenuPage")
    public String addMenuPage(Integer grade, Integer pId, Model model){
        model.addAttribute("grade",grade);
        model.addAttribute("pId",pId);
        return "menu/add";
    }

    @RequestMapping("save")
    @ResponseBody
    public RespBean saveMenu(Menu menu){
        menuService.saveMenu(menu);
        return RespBean.success("菜单添加成功");
    }

    @RequestMapping("updateMenuPage")
    public String updateMenuPage(Integer id, Model model){
        model.addAttribute("menu",menuService.getById(id));
        return "menu/update";
    }

    @RequestMapping("update")
    @ResponseBody
    public RespBean updateMenu(Menu menu){
        menuService.updateMenu(menu);
        return RespBean.success("菜单记录更新成功！");
    }

    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteMenu(Integer id){
        menuService.deleteMenuById(id);
        return RespBean.success("菜单删除成功！");
    }

}
