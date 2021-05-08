package com.xxs.jxcadmin.query;

import lombok.Data;

/**
 * @author 13421
 */
@Data
public class BaseQuery {

    private final Integer page = 1;
    private final Integer limit = 10;
}
