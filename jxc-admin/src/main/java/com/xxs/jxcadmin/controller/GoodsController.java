package com.xxs.jxcadmin.controller;


import com.xxs.jxcadmin.model.RespBean;
import com.xxs.jxcadmin.pojo.Goods;
import com.xxs.jxcadmin.pojo.GoodsType;
import com.xxs.jxcadmin.query.GoodsQuery;
import com.xxs.jxcadmin.service.IGoodsService;
import com.xxs.jxcadmin.service.IGoodsTypeService;
import io.swagger.models.auth.In;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    IGoodsService goodsService;
    @Resource
    IGoodsTypeService goodsTypeService;


    @RequestMapping("index")
    public String index(){
        return "goods/goods";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> goodsList(GoodsQuery goodsQuery){
        return goodsService.goodsList(goodsQuery);
    }

    @RequestMapping("addOrUpdateGoodsPage")
    public String addOrUpdateGoodsPage(Integer id,Integer typeId, Model model){
        if(null!= id){
            Goods goods = goodsService.getById(id);
            GoodsType goodsType = goodsTypeService.getById(goods.getTypeId());
            model.addAttribute("goods",goods);
            model.addAttribute("goodsType",goodsType);
        }else{
            if(null != typeId){
                model.addAttribute("goodsType",goodsTypeService.getById(typeId));
            }
        }
        return "goods/add_update";
    }

    @RequestMapping("toGoodsTypePage")
    public String toGoodsTypePage(Integer typeId, Model model){
        if(null != typeId){
            model.addAttribute("typeId",typeId);
        }
        return "goods/goods_type";
    }

    @RequestMapping("save")
    @ResponseBody
    public RespBean saveGoods(Goods goods){
        goodsService.saveGoods(goods);
        return RespBean.success("保存成功！");
    }
    @RequestMapping("update")
    @ResponseBody
    public RespBean updateGoods(Goods goods){
        goodsService.updateGoods(goods);
        return RespBean.success("更新成功！");
    }
    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteGoods(Integer id){
        goodsService.deleteGoods(id);
        return RespBean.success("删除成功！");
    }


}
