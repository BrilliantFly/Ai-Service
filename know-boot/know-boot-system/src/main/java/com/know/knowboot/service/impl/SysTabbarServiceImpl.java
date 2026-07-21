package com.know.knowboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.tenant.SysTabbar;
import com.know.knowboot.mapper.tenant.SysTabbarMapper;
import com.know.knowboot.service.ISysTabbarService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysTabbarServiceImpl extends ServiceImpl<SysTabbarMapper, SysTabbar> implements ISysTabbarService {

    @Override
    public IPage<SysTabbar> page(SysTabbar query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<SysTabbar> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getName() != null, SysTabbar::getName, query.getName())
                .eq(query.getStatus() != null, SysTabbar::getStatus, query.getStatus())
                .orderByAsc(SysTabbar::getSort);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<SysTabbar> listAll() {
        return list(new LambdaQueryWrapper<SysTabbar>().orderByAsc(SysTabbar::getSort));
    }

    @Override
    public boolean add(SysTabbar tabbar) {
        return save(tabbar);
    }

    @Override
    public boolean update(SysTabbar tabbar) {
        return updateById(tabbar);
    }

    @Override
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public SysTabbar getById(Long id) {
        return baseMapper.selectById(id);
    }
}