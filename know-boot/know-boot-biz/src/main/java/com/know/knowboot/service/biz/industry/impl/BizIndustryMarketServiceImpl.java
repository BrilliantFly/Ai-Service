package com.know.knowboot.service.biz.industry.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.biz.industry.BizIndustry;
import com.know.knowboot.entity.biz.industry.BizIndustryMarket;
import com.know.knowboot.entity.biz.industry.BizIndustryMarketRel;
import com.know.knowboot.mapper.biz.industry.BizIndustryMapper;
import com.know.knowboot.mapper.biz.industry.BizIndustryMarketMapper;
import com.know.knowboot.mapper.biz.industry.BizIndustryMarketRelMapper;
import com.know.knowboot.service.biz.industry.IBizIndustryMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 行业市场服务实现
 */
@Service
public class BizIndustryMarketServiceImpl extends ServiceImpl<BizIndustryMarketMapper, BizIndustryMarket> implements IBizIndustryMarketService {

    @Autowired
    private BizIndustryMarketMapper bizIndustryMarketMapper;

    @Autowired
    private BizIndustryMarketRelMapper bizIndustryMarketRelMapper;

    @Autowired
    private BizIndustryMapper bizIndustryMapper;

    @Override
    public BizIndustryMarket getByIndustryId(Long industryId) {
        List<BizIndustryMarket> markets = getListByIndustryId(industryId);
        if (markets == null || markets.isEmpty()) {
            return null;
        }
        return markets.get(0);
    }

    @Override
    public List<BizIndustryMarket> getListByIndustryId(Long industryId) {
        List<Long> marketIds = getMarketIdsByIndustry(industryId);
        if (marketIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<BizIndustryMarket> markets = bizIndustryMarketMapper.selectBatchIds(marketIds);
        return markets == null ? new ArrayList<>() : markets;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(BizIndustryMarket market, Long userId) {
        if (market.getId() != null) {
            market.setUpdateBy(userId);
            market.setUpdateTime(System.currentTimeMillis());
            boolean updated = updateById(market);
            if (market.getIndustryIds() != null) {
                replaceIndustries(market.getId(), market.getIndustryIds(), userId);
            }
            return updated;
        }
        createMarket(market, userId);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createMarket(BizIndustryMarket market, Long userId) {
        long now = System.currentTimeMillis();
        market.setCreateBy(userId);
        market.setCreateTime(now);
        market.setDelFlag(0);
        market.setDeleteTime(0L);
        save(market);
        if (market.getIndustryIds() != null && !market.getIndustryIds().isEmpty()) {
            replaceIndustries(market.getId(), market.getIndustryIds(), userId);
        }
        return market.getId();
    }

    private List<Long> getMarketIdsByIndustry(Long industryId) {
        List<BizIndustryMarketRel> relations = bizIndustryMarketRelMapper.selectList(new LambdaQueryWrapper<BizIndustryMarketRel>()
                .eq(BizIndustryMarketRel::getIndustryId, industryId)
                .eq(BizIndustryMarketRel::getDelFlag, 0));
        List<Long> marketIds = new ArrayList<>();
        if (relations != null) {
            for (BizIndustryMarketRel relation : relations) {
                marketIds.add(relation.getMarketId());
            }
        }
        return marketIds;
    }

    private void replaceIndustries(Long marketId, List<Long> industryIds, Long userId) {
        long now = System.currentTimeMillis();
        if (industryIds != null) {
            for (Long industryId : industryIds) {
                BizIndustry industry = bizIndustryMapper.selectById(industryId);
                if (industry == null) {
                    throw new IllegalArgumentException("行业不存在: id=" + industryId);
                }
            }
        }
        // 物理删除旧关联(含逻辑删除残留),避免唯一索引冲突
        bizIndustryMarketRelMapper.hardDeleteByMarketId(marketId);
        if (industryIds != null) {
            for (Long industryId : industryIds) {
                BizIndustryMarketRel relation = new BizIndustryMarketRel();
                relation.setIndustryId(industryId);
                relation.setMarketId(marketId);
                relation.setCreateBy(userId);
                relation.setCreateTime(now);
                relation.setDelFlag(0);
                relation.setDeleteTime(0L);
                bizIndustryMarketRelMapper.insert(relation);
            }
        }
    }
}