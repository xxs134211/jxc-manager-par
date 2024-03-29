package com.xxs.jxcadmin.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品类别表
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_goods_type")
@ApiModel(value="GoodsType对象", description="商品类别表")
public class GoodsType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "类别名")
    private String name;

    @ApiModelProperty(value = "父级类别id")
    private Integer pId;

    @ApiModelProperty(value = "节点类型")
    private Integer state;

    @ApiModelProperty(value = "节点图标")
    private String icon;


}
