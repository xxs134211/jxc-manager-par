package com.xxs.jxcadmin.service.impl;

import com.xxs.jxcadmin.pojo.UserRole;
import com.xxs.jxcadmin.mapper.UserRoleMapper;
import com.xxs.jxcadmin.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author xxs
 * @since 2021-05-07
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Override
    public List<String> findRolesByUserName(String userName) {
        return this.baseMapper.findRolesByUserName(userName);
    }
}
