package com.xxs.jxcadmin.service;

import com.xxs.jxcadmin.pojo.Supplier;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxs.jxcadmin.query.SupplierQuery;

import java.util.Map;

/**
 * <p>
 * 供应商表 服务类
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
public interface ISupplierService extends IService<Supplier> {

    Map<String, Object> supplierList(SupplierQuery supplierQuery);

    void saveSupplier(Supplier supplier);
    Supplier findSupplierByName(String supplierName);

    void updateSupplier(Supplier supplier);

    void deleteSupplier(Integer[] ids);
}
