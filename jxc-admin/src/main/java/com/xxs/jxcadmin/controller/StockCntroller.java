package com.xxs.jxcadmin.controller;

import com.xxs.jxcadmin.model.RespBean;
import com.xxs.jxcadmin.pojo.Goods;
import com.xxs.jxcadmin.query.GoodsQuery;
import com.xxs.jxcadmin.service.IGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 13421
 */
@Controller
@RequestMapping("/stock")
public class StockCntroller {
    @Resource
    private IGoodsService goodsService;

    @RequestMapping("index")
    public String index(){
        return "stock/stock";
    }


    @RequestMapping("toUpdateGoodsInfoPage")
    public String toUpdateGoodsInfoPage(Integer gid, Model model){
        model.addAttribute("goods",goodsService.getById(gid));
        return "stock/goods_update";
    }

    @RequestMapping("listHasInventoryQuantity")
    @ResponseBody
    public Map<String,Object> listHasInventoryQuantity(GoodsQuery goodsQuery){
        goodsQuery.setType(2);
        return goodsService.goodsList(goodsQuery);
    }

    @RequestMapping("listNoInventoryQuantity")
    @ResponseBody
    public Map<String, Object> listNoInventoryQuantity(GoodsQuery goodsQuery){
        goodsQuery.setType(1);
        return goodsService.goodsList(goodsQuery);
    }

    @RequestMapping("updateStock")
    @ResponseBody
    public RespBean updateGoods(Goods goods){
        goodsService.updateStock(goods);
        return RespBean.success("商品记录更新成功！");
    }

    @RequestMapping("deleteStock")
    @ResponseBody
    public RespBean deleteGoods(Integer id){
        goodsService.deleteStock(id);
        return RespBean.success("商品记录删除成功！");
    }
}
