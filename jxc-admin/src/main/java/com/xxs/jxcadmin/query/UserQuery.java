package com.xxs.jxcadmin.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 13421
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQuery extends BaseQuery{
    private String userName;
}
