package com.xxs.jxcadmin.service;

import com.xxs.jxcadmin.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxs.jxcadmin.query.RoleQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author xxs
 * @since 2021-05-07
 */
public interface IRoleService extends IService<Role> {

    Map<String, Object> roleList(RoleQuery roleQuery);

    Role findRoleByRoleName(String roleName);

    void saveRole(Role role);

    void updateRole(Role role);

    void deleteRole(Integer id);

    List<Map<String, Object>> queryAllRoles(Integer userId);

    void addGrant(Integer roleId, Integer[] mids);
}
