package com.xxs.jxcadmin.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 乐字节  踏实教育 用心服务
 *
 * @author 乐字节--老李
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerQuery extends BaseQuery{
    private String customerName;
}
