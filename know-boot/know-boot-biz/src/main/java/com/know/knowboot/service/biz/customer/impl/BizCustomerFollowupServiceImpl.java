package com.know.knowboot.service.biz.customer.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.biz.customer.BizCustomerFollowup;
import com.know.knowboot.mapper.biz.customer.BizCustomerFollowupMapper;
import com.know.knowboot.service.biz.customer.IBizCustomerFollowupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 客户跟进服务实现
 */
@Service
public class BizCustomerFollowupServiceImpl extends ServiceImpl<BizCustomerFollowupMapper, BizCustomerFollowup> implements IBizCustomerFollowupService {

    @Autowired
    private BizCustomerFollowupMapper bizCustomerFollowupMapper;

    @Override
    public IPage<BizCustomerFollowup> page(BizCustomerFollowup query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<BizCustomerFollowup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(query.getCustomerId() != null, BizCustomerFollowup::getCustomerId, query.getCustomerId())
                .eq(query.getType() != null, BizCustomerFollowup::getType, query.getType())
                .orderByDesc(BizCustomerFollowup::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public BizCustomerFollowup getDetail(Long id) {
        return bizCustomerFollowupMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(BizCustomerFollowup followup, Long userId) {
        followup.setCreateUser(userId);
        followup.setCreateTime(System.currentTimeMillis());
        save(followup);
        return followup.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(BizCustomerFollowup followup, Long userId) {
        return updateById(followup);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        return removeById(id);
    }
}