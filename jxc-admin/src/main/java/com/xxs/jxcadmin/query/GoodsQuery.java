package com.xxs.jxcadmin.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 乐字节  踏实教育 用心服务
 *
 * @author 乐字节--老李
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsQuery extends BaseQuery{
    private String goodsName;
    private Integer typeId;

    private List<Integer> typeIds;

    // 查询类型 区分库存量是否大于0查询
    /**
     * 1 库存量=0
     * 2 库存量>0
     */
    private Integer type;

}
