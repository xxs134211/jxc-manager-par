package com.xxs.jxcadmin.service.impl;

import com.xxs.jxcadmin.service.IRbacService;
import com.xxs.jxcadmin.service.IRoleMenuService;
import com.xxs.jxcadmin.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
/**
 * @author 13421
 */
@Service
public class RbacServiceImpl implements IRbacService {
    @Resource
    private IUserRoleService userRoleService;
    @Resource
    private IRoleMenuService roleMenuService;

    @Override
    public List<String> findRolesByUserName(String userName) {
        return userRoleService.findRolesByUserName(userName);
    }

    @Override
    public List<String> findAuthoritiesByRoleName(List<String> roleNames) {
        return roleMenuService.findAuthoritiesByRoleName(roleNames);
    }
}
