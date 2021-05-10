package com.xxs.jxcadmin.service;

import com.xxs.jxcadmin.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxs.jxcadmin.query.GoodsQuery;

import java.util.Map;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
public interface IGoodsService extends IService<Goods> {

    Map<String, Object> goodsList(GoodsQuery goodsQuery);

    void saveGoods(Goods goods);
    String genGoodsCode();

    void updateGoods(Goods goods);

    void deleteGoods(Integer id);

    void updateStock(Goods goods);

    void deleteStock(Integer id);
}
