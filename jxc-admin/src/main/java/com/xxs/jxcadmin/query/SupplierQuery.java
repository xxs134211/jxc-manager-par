package com.xxs.jxcadmin.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 乐字节  踏实教育 用心服务
 *
 * @author 乐字节--老李
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class SupplierQuery extends BaseQuery{
    private String supplierName;
}
