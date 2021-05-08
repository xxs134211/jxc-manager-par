package com.xxs.jxcadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxs.jxcadmin.pojo.Role;
import com.xxs.jxcadmin.mapper.RoleMapper;
import com.xxs.jxcadmin.pojo.User;
import com.xxs.jxcadmin.pojo.UserRole;
import com.xxs.jxcadmin.query.RoleQuery;
import com.xxs.jxcadmin.query.UserQuery;
import com.xxs.jxcadmin.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxs.jxcadmin.service.IUserRoleService;
import com.xxs.jxcadmin.utils.AssertUtil;
import com.xxs.jxcadmin.utils.PageResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author xxs
 * @since 2021-05-07
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public Map<String, Object> roleList(RoleQuery roleQuery) {
        IPage<Role> page = new Page<>(roleQuery.getPage(),roleQuery.getLimit());
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del",0);
        if(StringUtils.isNotBlank(roleQuery.getRoleName())){
            queryWrapper.like("name",roleQuery.getRoleName());
        }
        page = this.baseMapper.selectPage(page, queryWrapper);
        return PageResultUtil.getResult(page.getTotal(),page.getRecords());
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        return this.baseMapper.selectOne(new QueryWrapper<Role>().eq("is_del", 0).eq("name", roleName));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveRole(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getName()),"角色名称不能为空！");
        AssertUtil.isTrue(null != this.findRoleByRoleName(role.getName()),"角色名称已存在！");
        role.setIsDel(0);
        AssertUtil.isTrue(!this.save(role),"角色添加失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateRole(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getName()),"角色名称不能为空！");
        Role temp = this.findRoleByRoleName(role.getName());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(role.getId())),"角色名称已存在！");
        AssertUtil.isTrue(!this.updateById(role),"角色更新失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteRole(Integer id) {
        AssertUtil.isTrue(null == id,"请选择要删除的记录！");
        int count = userRoleService.count(new QueryWrapper<UserRole>().in("role_id", id));
        if(count>0){
            AssertUtil.isTrue(!(userRoleService.remove(new QueryWrapper<UserRole>().in("role_id",id))),"用户角色记录删除失败！");
        }
        Role temp = this.getById(id);
        temp.setIsDel(1);
        AssertUtil.isTrue(!(this.updateById(temp)),"删除成功！");
    }


    @Override
    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return this.baseMapper.queryAllRoles(userId);
    }

}
