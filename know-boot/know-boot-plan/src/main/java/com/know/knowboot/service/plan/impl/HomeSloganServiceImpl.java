package com.know.knowboot.service.plan.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.plan.HomeSlogan;
import com.know.knowboot.mapper.plan.HomeSloganMapper;
import com.know.knowboot.service.plan.IHomeSloganService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 首页标语服务实现
 */
@Service
public class HomeSloganServiceImpl extends ServiceImpl<HomeSloganMapper, HomeSlogan> implements IHomeSloganService {

    @Override
    public IPage<HomeSlogan> page(HomeSlogan query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<HomeSlogan> wrapper = buildQueryWrapper(query);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<HomeSlogan> list(HomeSlogan query) {
        LambdaQueryWrapper<HomeSlogan> wrapper = buildQueryWrapper(query);
        wrapper.orderByAsc(HomeSlogan::getSort);
        return list(wrapper);
    }

    private LambdaQueryWrapper<HomeSlogan> buildQueryWrapper(HomeSlogan query) {
        LambdaQueryWrapper<HomeSlogan> wrapper = new LambdaQueryWrapper<>();
        if (query != null) {
            wrapper.like(StringUtils.hasText(query.getContent()), HomeSlogan::getContent, query.getContent());
            wrapper.eq(query.getStatus() != null, HomeSlogan::getStatus, query.getStatus());
        }
        wrapper.orderByAsc(HomeSlogan::getSort);
        wrapper.orderByDesc(HomeSlogan::getUpdateTime);
        return wrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(HomeSlogan slogan, Long userId) {
        long now = System.currentTimeMillis();
        slogan.setId(null);
        slogan.setDelFlag(0);
        if (slogan.getSort() == null) {
            slogan.setSort(0);
        }
        if (slogan.getStatus() == null) {
            slogan.setStatus(1);
        }
        slogan.setCreateBy(userId);
        slogan.setCreateTime(now);
        slogan.setUpdateBy(userId);
        slogan.setUpdateTime(now);
        return save(slogan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(HomeSlogan slogan) {
        if (slogan.getId() == null) {
            return false;
        }
        slogan.setUpdateTime(System.currentTimeMillis());
        return updateById(slogan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public HomeSlogan getCurrent() {
        long now = System.currentTimeMillis();
        // 1. 优先返回当前时间窗内生效的定时标语（支持定时切换）
        HomeSlogan timed = getOne(new LambdaQueryWrapper<HomeSlogan>()
                .eq(HomeSlogan::getStatus, 1)
                .isNotNull(HomeSlogan::getStartTime)
                .isNotNull(HomeSlogan::getEndTime)
                .le(HomeSlogan::getStartTime, now)
                .ge(HomeSlogan::getEndTime, now)
                .orderByAsc(HomeSlogan::getSort)
                .last("LIMIT 1"));
        if (timed != null) {
            return timed;
        }
        // 2. 否则返回永久生效（未设置时间窗）的兜底标语
        return getOne(new LambdaQueryWrapper<HomeSlogan>()
                .eq(HomeSlogan::getStatus, 1)
                .isNull(HomeSlogan::getStartTime)
                .isNull(HomeSlogan::getEndTime)
                .orderByAsc(HomeSlogan::getSort)
                .last("LIMIT 1"));
    }
}
