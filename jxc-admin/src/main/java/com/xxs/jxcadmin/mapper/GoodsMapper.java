package com.xxs.jxcadmin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xxs.jxcadmin.pojo.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxs.jxcadmin.query.GoodsQuery;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
public interface GoodsMapper extends BaseMapper<Goods> {


    IPage<Goods> queryGoodsByParams(IPage<Goods> page, @Param("goodsQuery")GoodsQuery goodsQuery);
}
