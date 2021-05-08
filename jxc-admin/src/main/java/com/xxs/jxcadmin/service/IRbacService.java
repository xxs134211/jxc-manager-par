package com.xxs.jxcadmin.service;

import java.util.List;

/**
 * @author 13421
 */
public interface IRbacService {
    List<String> findRolesByUserName(String name);

    List<String> findAuthoritiesByRoleName(List<String> roleNames);
}
