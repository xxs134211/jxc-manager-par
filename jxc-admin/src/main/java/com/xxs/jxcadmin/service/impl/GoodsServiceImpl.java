package com.xxs.jxcadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxs.jxcadmin.pojo.Goods;
import com.xxs.jxcadmin.mapper.GoodsMapper;
import com.xxs.jxcadmin.query.GoodsQuery;
import com.xxs.jxcadmin.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxs.jxcadmin.service.IGoodsTypeService;
import com.xxs.jxcadmin.utils.AssertUtil;
import com.xxs.jxcadmin.utils.PageResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Resource
    private IGoodsTypeService goodsTypeService;

    @Override
    public Map<String, Object> goodsList(GoodsQuery goodsQuery) {
        IPage<Goods> page = new Page<>(goodsQuery.getPage(),goodsQuery.getLimit());

        if(null != goodsQuery.getTypeId()){
            goodsQuery.setTypeIds(goodsTypeService.queryAllSubTypeIdsByTypeId(goodsQuery.getTypeId()));
        }

        page = this.baseMapper.queryGoodsByParams(page,goodsQuery);

        return PageResultUtil.getResult(page.getTotal(),page.getRecords());
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveGoods(Goods goods) {
        AssertUtil.isTrue(StringUtils.isBlank(goods.getName()),"商品名称不能为空！");
        AssertUtil.isTrue(null == goods.getTypeId(),"请指定商品类别！");
        AssertUtil.isTrue(StringUtils.isBlank(goods.getUnit()),"请指定商品单位！");
        goods.setCode(genGoodsCode());
        goods.setInventoryQuantity(0);
        goods.setState(0);
        goods.setLastPurchasingPrice(0F);
        goods.setIsDel(0);
        AssertUtil.isTrue(!(this.save(goods)),"记录添加失败！");

    }

    @Override
    public String genGoodsCode() {
        String maxGoodsCode = this.baseMapper.selectOne(new QueryWrapper<Goods>().select("max(code) as code")).getCode();
        if(StringUtils.isNotBlank(maxGoodsCode)){
            int code = Integer.parseInt(maxGoodsCode) + 1;
            StringBuilder codes = new StringBuilder(Integer.toString(code));
            int length = codes.length();
            for (int i = 4; i > length; i--) {
                codes.insert(0, "0");
            }
            return codes.toString();
        }else{
            return "0001";
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateGoods(Goods goods) {
        AssertUtil.isTrue(StringUtils.isBlank(goods.getName()),"商品名称不能为空！");
        AssertUtil.isTrue(null == goods.getTypeId(),"请指定商品类别！");
        AssertUtil.isTrue(StringUtils.isBlank(goods.getUnit()),"请指定商品单位！");
        AssertUtil.isTrue(!(this.updateById(goods)),"更新成功！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteGoods(Integer id) {
        Goods goods = this.getById(id);
        AssertUtil.isTrue(null == goods,"商品不存在，无法删除！");
        AssertUtil.isTrue(goods.getState()==1,"商品已经期初入库，不能删除！");
        AssertUtil.isTrue(goods.getState()==2,"该商品已经存在单据，不能删除！");
        goods.setIsDel(1);
        AssertUtil.isTrue(!(this.updateById(goods)),"商品删除失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateStock(Goods goods) {
        Goods temp = this.getById(goods.getId());
        AssertUtil.isTrue(null == temp,"待更新的商品记录不存在！");
        AssertUtil.isTrue(goods.getInventoryQuantity()<=0,"库存必须>0");
        AssertUtil.isTrue(goods.getPurchasingPrice()<=0,"成本价必须>0");
        AssertUtil.isTrue(!(this.updateById(goods)),"商品更新失败！");
    }

    @Override
    public void deleteStock(Integer id) {
        Goods temp = this.getById(id);
        AssertUtil.isTrue(null == temp,"待删除的商品记录不存在！");
        AssertUtil.isTrue(temp.getState()==2,"该商品已经存在单据，不能删除！");
        temp.setInventoryQuantity(0);
        AssertUtil.isTrue(!(this.updateById(temp)),"商品删除失败！");

    }
}
