package com.know.knowboot.service.biz.customer.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.biz.customer.BizCustomerCompany;
import com.know.knowboot.mapper.biz.customer.BizCustomerCompanyMapper;
import com.know.knowboot.service.biz.customer.IBizCustomerCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 客户企业服务实现
 */
@Service
public class BizCustomerCompanyServiceImpl extends ServiceImpl<BizCustomerCompanyMapper, BizCustomerCompany> implements IBizCustomerCompanyService {

    @Autowired
    private BizCustomerCompanyMapper bizCustomerCompanyMapper;

    @Override
    public IPage<BizCustomerCompany> page(BizCustomerCompany query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<BizCustomerCompany> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getId() != null, BizCustomerCompany::getId, query.getId())
                .like(query.getName() != null, BizCustomerCompany::getName, query.getName())
                .orderByDesc(BizCustomerCompany::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public BizCustomerCompany getDetail(Long id) {
        return bizCustomerCompanyMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(BizCustomerCompany company, Long userId) {
        company.setCreateBy(userId);
        company.setCreateTime(System.currentTimeMillis());
        company.setDeleted(0);
        company.setDeleteTime(0L);
        save(company);
        return company.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(BizCustomerCompany company, Long userId) {
        company.setUpdateBy(userId);
        company.setUpdateTime(System.currentTimeMillis());
        return updateById(company);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        return removeById(id);
    }
}