package com.xxs.jxcadmin.service;

import com.xxs.jxcadmin.pojo.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxs.jxcadmin.query.CustomerQuery;

import java.util.Map;

/**
 * <p>
 * 客户表 服务类
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
public interface ICustomerService extends IService<Customer> {

    Map<String, Object> customerList(CustomerQuery customerQuery);

    void saveCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomer(Integer[] ids);

    Customer findCustomerByName(String name);
}
