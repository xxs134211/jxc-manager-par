package com.xxs.jxcadmin.mapper;

import com.xxs.jxcadmin.dto.TreeDto;
import com.xxs.jxcadmin.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<TreeDto> queryAllMenus();
}
