package com.xxs.jxcadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxs.jxcadmin.pojo.Customer;
import com.xxs.jxcadmin.mapper.CustomerMapper;
import com.xxs.jxcadmin.pojo.Supplier;
import com.xxs.jxcadmin.query.CustomerQuery;
import com.xxs.jxcadmin.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxs.jxcadmin.utils.AssertUtil;
import com.xxs.jxcadmin.utils.PageResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author xxs
 * @since 2021-05-08
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Override
    public Map<String, Object> customerList(CustomerQuery customerQuery) {
        IPage<Customer> page = new Page<>(customerQuery.getPage(),customerQuery.getLimit());
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del",0);
        if(StringUtils.isNotBlank(customerQuery.getCustomerName())){
            queryWrapper.like("name",customerQuery.getCustomerName());
        }
        page = this.baseMapper.selectPage(page,queryWrapper);
        return PageResultUtil.getResult(page.getTotal(),page.getRecords());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveCustomer(Customer customer) {
        AssertUtil.isTrue(StringUtils.isBlank(customer.getName()),"名称不能为空！");
        AssertUtil.isTrue(null != this.findCustomerByName(customer.getName()),"客户已存在！");
        customer.setIsDel(0);
        AssertUtil.isTrue(!this.save(customer),"添加失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateCustomer(Customer customer) {
        AssertUtil.isTrue(StringUtils.isBlank(customer.getName()),"名称不能为空！");
        Customer temp = this.findCustomerByName(customer.getName());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(customer.getId())),"客户已存在！");
        AssertUtil.isTrue(!this.updateById(customer),"客户更新失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteCustomer(Integer[] ids) {
        AssertUtil.isTrue(null == ids || ids.length==0,"请选择要删除的记录！");
        List<Customer> customers = new ArrayList<>();
        assert ids != null;
        for (Integer id : ids) {
            Customer temp = this.getById(id);
            temp.setIsDel(1);
            customers.add(temp);
        }
        AssertUtil.isTrue(!(this.updateBatchById(customers)),"删除成功！");
    }

    @Override
    public Customer findCustomerByName(String name) {
        return this.baseMapper.selectOne(new QueryWrapper<Customer>().eq("is_del",0).eq("name",name));
    }
}
