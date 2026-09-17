package com.know.knowboot.service.biz.industry.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public IPage<BizIndustryMarket> page(Long industryId, String keyword, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<BizIndustryMarket> wrapper = new LambdaQueryWrapper<>();
        if (industryId != null) {
            List<Long> marketIds = getMarketIdsByIndustry(industryId);
            if (marketIds.isEmpty()) {
                return new Page<>(pageNum, pageSize);
            }
            wrapper.in(BizIndustryMarket::getId, marketIds);
        }
        // 关键字同时匹配 需求 或 机会(AND 包裹 OR),避免链式 like 生成 AND 语义
        wrapper.and(keyword != null, w -> w
                        .like(BizIndustryMarket::getDemand, keyword)
                        .or()
                        .like(BizIndustryMarket::getOpportunity, keyword))
                .orderByDesc(BizIndustryMarket::getCreateTime);
        IPage<BizIndustryMarket> result = page(new Page<>(pageNum, pageSize), wrapper);
        fillIndustryRelations(result.getRecords());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteMarket(Long id) {
        // 市场本身逻辑删除(@TableLogic)
        removeById(id);
        long now = System.currentTimeMillis();
        // 级联软删行业关联
        bizIndustryMarketRelMapper.update(null, new LambdaUpdateWrapper<BizIndustryMarketRel>()
                .eq(BizIndustryMarketRel::getMarketId, id)
                .set(BizIndustryMarketRel::getDelFlag, 1)
                .set(BizIndustryMarketRel::getDeleteTime, now));
        return Boolean.TRUE;
    }

    // 分页结果填充关联行业(避免列表页无关联信息)
    private void fillIndustryRelations(List<BizIndustryMarket> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        List<Long> marketIds = new ArrayList<>();
        for (BizIndustryMarket record : records) {
            marketIds.add(record.getId());
        }
        List<BizIndustryMarketRel> rels = bizIndustryMarketRelMapper.selectList(new LambdaQueryWrapper<BizIndustryMarketRel>()
                .in(BizIndustryMarketRel::getMarketId, marketIds)
                .eq(BizIndustryMarketRel::getDelFlag, 0));
        Map<Long, List<Long>> marketIndustryMap = new HashMap<>();
        if (rels != null) {
            for (BizIndustryMarketRel rel : rels) {
                marketIndustryMap.computeIfAbsent(rel.getMarketId(), k -> new ArrayList<>()).add(rel.getIndustryId());
            }
        }
        for (BizIndustryMarket record : records) {
            List<Long> industryIds = marketIndustryMap.getOrDefault(record.getId(), new ArrayList<>());
            record.setIndustryIds(industryIds);
            record.setIndustryNames(queryIndustryNames(industryIds));
        }
    }

    private List<String> queryIndustryNames(List<Long> industryIds) {
        List<String> industryNames = new ArrayList<>();
        if (industryIds == null || industryIds.isEmpty()) {
            return industryNames;
        }
        Map<Long, String> nameMap = new HashMap<>();
        List<BizIndustry> industryList = bizIndustryMapper.selectBatchIds(industryIds);
        if (industryList != null) {
            for (BizIndustry industry : industryList) {
                nameMap.put(industry.getId(), industry.getIndustryName());
            }
        }
        for (Long industryId : industryIds) {
            industryNames.add(nameMap.get(industryId));
        }
        return industryNames;
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