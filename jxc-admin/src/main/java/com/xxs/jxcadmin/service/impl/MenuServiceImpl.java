package com.xxs.jxcadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xxs.jxcadmin.dto.TreeDto;
import com.xxs.jxcadmin.pojo.Menu;
import com.xxs.jxcadmin.mapper.MenuMapper;
import com.xxs.jxcadmin.pojo.RoleMenu;
import com.xxs.jxcadmin.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxs.jxcadmin.service.IRoleMenuService;
import com.xxs.jxcadmin.utils.AssertUtil;
import com.xxs.jxcadmin.utils.PageResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    private IRoleMenuService roleMenuService;
    @Override
    public List<TreeDto> queryAllMenus(Integer roleId) {
        List<TreeDto> treeDtos = this.baseMapper.queryAllMenus();
        List<Integer> roleHasMenusIds = roleMenuService.queryRoleHasAllMenusByRoleId(roleId);
        if(CollectionUtils.isNotEmpty(roleHasMenusIds)){
            treeDtos.forEach(treeDto -> {
                if(roleHasMenusIds.contains(treeDto.getId())){
                    treeDto.setChecked(true);
                }
            });
        }
        return treeDtos;
    }

    @Override
    public Map<String, Object> menuList() {
        List<Menu> menus = this.list(new QueryWrapper<Menu>().eq("is_del",0));
        return PageResultUtil.getResult((long) menus.size(),menus);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveMenu(Menu menu) {
        AssertUtil.isTrue(StringUtils.isBlank(menu.getName()),"请输入菜单名称！");
        Integer grade = menu.getGrade();
        AssertUtil.isTrue(null==grade||!(grade==0||grade==1||grade==2),"菜单层级不合法！");
        AssertUtil.isTrue(null!=this.findMenuByNameAndGrade(menu.getName(),menu.getGrade()),"该层级下菜单已存在！");
        AssertUtil.isTrue(null != this.findMenuByAclValue(menu.getAclValue()),"权限码已存在！");
        AssertUtil.isTrue(null == menu.getPId()|| null== this.findMenuById(menu.getPId()),"请指定上级菜单！");

        if(grade==1){
            AssertUtil.isTrue(null!= menu.getUrl()&& null != this.findMenuByGradeAndUrl(menu.getUrl(),menu.getGrade()),"该层级下url不可重复！");
        }

        menu.setIsDel(0);
        menu.setState(0);
        AssertUtil.isTrue(!(this.save(menu)),"菜单添加失败！");
    }

    @Override
    public Menu findMenuByNameAndGrade(String name,Integer grade) {
        return this.getOne(new QueryWrapper<Menu>().eq("is_del",0).eq("name",name).eq("grade",grade));
    }

    @Override
    public Menu findMenuByAclValue(String aclValue) {
        return this.getOne(new QueryWrapper<Menu>().eq("is_del",0).eq("acl_value",aclValue));
    }

    @Override
    public Menu findMenuById(Integer id) {
        return getOne(new QueryWrapper<Menu>().eq("is_del",0).eq("id",id));
    }

    @Override
    public Menu findMenuByGradeAndUrl(String url, Integer grade) {
        return getOne(new QueryWrapper<Menu>().eq("is_del",0).eq("url",url).eq("grade",grade));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateMenu(Menu menu) {
        AssertUtil.isTrue(null==menu.getId()||null == this.findMenuById(menu.getId()),"请输入菜单名称！");
        AssertUtil.isTrue(StringUtils.isBlank(menu.getName()),"请输入菜单名称！");
        Integer grade = menu.getGrade();
        AssertUtil.isTrue(null==grade||!(grade==0||grade==1||grade==2),"菜单层级不合法！");
        Menu temp = this.findMenuByNameAndGrade(menu.getName(), menu.getGrade());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(menu.getId())),"该层级下菜单已存在!");
        temp = this.findMenuByAclValue(menu.getAclValue());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(menu.getId())),"权限码已存在!");
        AssertUtil.isTrue(null == menu.getPId()|| null== this.findMenuById(menu.getPId()),"请指定上级菜单！");
        if(grade==1){
            temp = this.findMenuByGradeAndUrl(menu.getUrl(),menu.getGrade());
            AssertUtil.isTrue(null != temp && !(temp.getId().equals(menu.getId())),"该层级下url不可重复！");
        }
        AssertUtil.isTrue(!(this.updateById(menu)),"菜单更新失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteMenuById(Integer id) {
        Menu menu = this.findMenuById(id);
        AssertUtil.isTrue(null == id || null == menu,"待删除的记录不存在！");
        int count = this.count(new QueryWrapper<Menu>().eq("is_del",0).eq("p_id",id));
        AssertUtil.isTrue(count>0,"存在子菜单，不允许直接删除！");
        count = roleMenuService.count(new QueryWrapper<RoleMenu>().eq("menu_id",id));
        if(count>0){
            AssertUtil.isTrue(!(roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("menu_id",id))),"删除失败！");
        }
        assert menu != null;
        menu.setIsDel(1);
        AssertUtil.isTrue(!(this.updateById(menu)),"菜单删除失败！");

    }
}
