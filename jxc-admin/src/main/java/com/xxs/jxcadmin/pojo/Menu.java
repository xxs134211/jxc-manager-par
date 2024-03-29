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
 * 菜单表
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_menu")
@ApiModel(value="Menu对象", description="菜单表")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "节点类型")
    private Integer state;

    @ApiModelProperty(value = "菜单url")
    private String url;

    @ApiModelProperty(value = "上级菜单id")
    private Integer pId;

    @ApiModelProperty(value = "权限码")
    private String aclValue;

    @ApiModelProperty(value = "菜单层级")
    private Integer grade;

    @ApiModelProperty(value = "是否删除")
    private Integer isDel;


}
