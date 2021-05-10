package com.xxs.jxcadmin.service;

import com.xxs.jxcadmin.dto.TreeDto;
import com.xxs.jxcadmin.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
public interface IMenuService extends IService<Menu> {

    List<TreeDto> queryAllMenus(Integer roleId);

    Map<String, Object> menuList();

    void saveMenu(Menu menu);

    Menu findMenuByNameAndGrade(String name, Integer grade);
    Menu findMenuByAclValue(String aclValue);
    Menu findMenuById(Integer id);
    Menu findMenuByGradeAndUrl(String url,Integer grade);

    void updateMenu(Menu menu);

    void deleteMenuById(Integer id);
}
