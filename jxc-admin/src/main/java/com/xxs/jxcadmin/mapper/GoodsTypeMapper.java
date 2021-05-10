package com.xxs.jxcadmin.mapper;

import com.xxs.jxcadmin.dto.TreeDto;
import com.xxs.jxcadmin.pojo.GoodsType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 商品类别表 Mapper 接口
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
public interface GoodsTypeMapper extends BaseMapper<GoodsType> {

    List<TreeDto> queryAllGoodsTypes();
}
