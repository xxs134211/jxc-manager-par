package com.xxs.jxcadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxs.jxcadmin.pojo.Supplier;
import com.xxs.jxcadmin.mapper.SupplierMapper;
import com.xxs.jxcadmin.pojo.User;
import com.xxs.jxcadmin.pojo.UserRole;
import com.xxs.jxcadmin.query.SupplierQuery;
import com.xxs.jxcadmin.service.ISupplierService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxs.jxcadmin.utils.AssertUtil;
import com.xxs.jxcadmin.utils.PageResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 供应商表 服务实现类
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements ISupplierService {

    @Override
    public Map<String, Object> supplierList(SupplierQuery supplierQuery) {
        IPage<Supplier> page = new Page<>(supplierQuery.getPage(),supplierQuery.getLimit());
        QueryWrapper<Supplier> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del",0);
        if(StringUtils.isNotBlank(supplierQuery.getSupplierName())){
            queryWrapper.like("name",supplierQuery.getSupplierName());
        }
        page = this.baseMapper.selectPage(page,queryWrapper);

        return PageResultUtil.getResult(page.getTotal(),page.getRecords());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveSupplier(Supplier supplier) {
        AssertUtil.isTrue(StringUtils.isBlank(supplier.getName()),"名称不能为空！");
        AssertUtil.isTrue(null != this.findSupplierByName(supplier.getName()),"供应商已存在！");
        supplier.setIsDel(0);
        AssertUtil.isTrue(!this.save(supplier),"添加失败！");
    }

    @Override
    public Supplier findSupplierByName(String supplierName) {
        return this.baseMapper.selectOne(new QueryWrapper<Supplier>().eq("is_del",0).eq("name",supplierName));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateSupplier(Supplier supplier) {
        AssertUtil.isTrue(StringUtils.isBlank(supplier.getName()),"名称不能为空！");
        Supplier temp = this.findSupplierByName(supplier.getName());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(supplier.getId())),"供应商已存在！");
        AssertUtil.isTrue(!this.updateById(supplier),"供应商更新失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteSupplier(Integer[] ids) {
        AssertUtil.isTrue(null == ids || ids.length==0,"请选择要删除的记录！");
        List<Supplier> suppliers = new ArrayList<>();
        assert ids != null;
        for (Integer id : ids) {
            Supplier temp = this.getById(id);
            temp.setIsDel(1);
            suppliers.add(temp);
        }
        AssertUtil.isTrue(!(this.updateBatchById(suppliers)),"删除成功！");
    }
}
