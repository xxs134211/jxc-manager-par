package com.xxs.jxcadmin.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 13421
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleQuery extends BaseQuery{
    private String roleName;
}
