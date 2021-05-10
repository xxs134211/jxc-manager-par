package com.xxs.jxcadmin.controller;


import com.xxs.jxcadmin.pojo.GoodsUnit;
import com.xxs.jxcadmin.service.IGoodsUnitService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品单位表 前端控制器
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
@Controller
@RequestMapping("/goodsUnit")
public class GoodsUnitController {
    @Resource
    IGoodsUnitService goodsUnitService;

    @RequestMapping("allGoodsUnits")
    @ResponseBody
    public List<GoodsUnit> findAllGoodsUnits(){
        return goodsUnitService.list();
    }

}
