package com.know.knowboot.service.biz.industry.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.biz.industry.BizIndustry;
import com.know.knowboot.entity.biz.industry.BizIndustryProduct;
import com.know.knowboot.entity.biz.industry.BizIndustryProductRel;
import com.know.knowboot.mapper.biz.industry.BizIndustryMapper;
import com.know.knowboot.mapper.biz.industry.BizIndustryProductMapper;
import com.know.knowboot.mapper.biz.industry.BizIndustryProductRelMapper;
import com.know.knowboot.service.biz.industry.IBizIndustryProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行业产品服务实现
 */
@Service
public class BizIndustryProductServiceImpl extends ServiceImpl<BizIndustryProductMapper, BizIndustryProduct> implements IBizIndustryProductService {

    @Autowired
    private BizIndustryProductMapper bizIndustryProductMapper;

    @Autowired
    private BizIndustryProductRelMapper bizIndustryProductRelMapper;

    @Autowired
    private BizIndustryMapper bizIndustryMapper;

    @Override
    public IPage<BizIndustryProduct> page(Long industryId, String category, String productName, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<BizIndustryProduct> wrapper = new LambdaQueryWrapper<>();
        if (industryId != null) {
            List<Long> productIds = getProductIdsByIndustry(industryId);
            if (productIds.isEmpty()) {
                return new Page<>(pageNum, pageSize);
            }
            wrapper.in(BizIndustryProduct::getId, productIds);
        }
        wrapper.like(category != null, BizIndustryProduct::getCategory, category)
                .like(productName != null, BizIndustryProduct::getProductName, productName)
                .orderByDesc(BizIndustryProduct::getCreateTime);
        IPage<BizIndustryProduct> result = page(new Page<>(pageNum, pageSize), wrapper);
        fillIndustryRelations(result.getRecords());
        return result;
    }

    // 分页结果填充关联行业(避免列表页无关联信息)
    private void fillIndustryRelations(List<BizIndustryProduct> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        List<Long> productIds = new ArrayList<>();
        for (BizIndustryProduct record : records) {
            productIds.add(record.getId());
        }
        List<BizIndustryProductRel> rels = bizIndustryProductRelMapper.selectList(new LambdaQueryWrapper<BizIndustryProductRel>()
                .in(BizIndustryProductRel::getProductId, productIds)
                .eq(BizIndustryProductRel::getDelFlag, 0));
        Map<Long, List<Long>> productIndustryMap = new HashMap<>();
        if (rels != null) {
            for (BizIndustryProductRel rel : rels) {
                productIndustryMap.computeIfAbsent(rel.getProductId(), k -> new ArrayList<>()).add(rel.getIndustryId());
            }
        }
        for (BizIndustryProduct record : records) {
            List<Long> industryIds = productIndustryMap.getOrDefault(record.getId(), new ArrayList<>());
            record.setIndustryIds(industryIds);
            record.setIndustryNames(queryIndustryNames(industryIds));
        }
    }

    @Override
    public BizIndustryProduct getDetail(Long id) {
        BizIndustryProduct product = bizIndustryProductMapper.selectById(id);
        if (product == null) {
            return null;
        }
        List<BizIndustryProductRel> relations = bizIndustryProductRelMapper.selectList(new LambdaQueryWrapper<BizIndustryProductRel>()
                .eq(BizIndustryProductRel::getProductId, id)
                .eq(BizIndustryProductRel::getDelFlag, 0));
        List<Long> industryIds = new ArrayList<>();
        if (relations != null) {
            for (BizIndustryProductRel relation : relations) {
                industryIds.add(relation.getIndustryId());
            }
        }
        product.setIndustryIds(industryIds);
        product.setIndustryNames(queryIndustryNames(industryIds));
        return product;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(BizIndustryProduct product, Long userId) {
        long now = System.currentTimeMillis();
        product.setCreateBy(userId);
        product.setCreateTime(now);
        product.setDelFlag(0);
        product.setDeleteTime(0L);
        save(product);
        if (product.getIndustryIds() != null && !product.getIndustryIds().isEmpty()) {
            for (Long industryId : product.getIndustryIds()) {
                validateIndustry(industryId);
                insertRel(product.getId(), industryId, userId, now);
            }
        }
        return product.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(BizIndustryProduct product, Long userId) {
        product.setUpdateBy(userId);
        product.setUpdateTime(System.currentTimeMillis());
        updateById(product);
        if (product.getIndustryIds() != null) {
            replaceIndustries(product.getId(), product.getIndustryIds(), userId);
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        removeById(id);
        long now = System.currentTimeMillis();
        bizIndustryProductRelMapper.update(null, new LambdaUpdateWrapper<BizIndustryProductRel>()
                .eq(BizIndustryProductRel::getProductId, id)
                .set(BizIndustryProductRel::getDelFlag, 1)
                .set(BizIndustryProductRel::getDeleteTime, now));
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean setIndustries(Long id, List<Long> industryIds, Long userId) {
        replaceIndustries(id, industryIds, userId);
        return Boolean.TRUE;
    }

    private List<Long> getProductIdsByIndustry(Long industryId) {
        List<BizIndustryProductRel> relations = bizIndustryProductRelMapper.selectList(new LambdaQueryWrapper<BizIndustryProductRel>()
                .eq(BizIndustryProductRel::getIndustryId, industryId)
                .eq(BizIndustryProductRel::getDelFlag, 0));
        List<Long> productIds = new ArrayList<>();
        if (relations != null) {
            for (BizIndustryProductRel relation : relations) {
                productIds.add(relation.getProductId());
            }
        }
        return productIds;
    }

    private void replaceIndustries(Long productId, List<Long> industryIds, Long userId) {
        long now = System.currentTimeMillis();
        if (industryIds != null) {
            for (Long industryId : industryIds) {
                validateIndustry(industryId);
            }
        }
        // 物理删除旧关联(含逻辑删除残留),避免唯一索引冲突
        bizIndustryProductRelMapper.hardDeleteByProductId(productId);
        if (industryIds != null) {
            for (Long industryId : industryIds) {
                insertRel(productId, industryId, userId, now);
            }
        }
    }

    private void insertRel(Long productId, Long industryId, Long userId, long now) {
        BizIndustryProductRel relation = new BizIndustryProductRel();
        relation.setIndustryId(industryId);
        relation.setProductId(productId);
        relation.setCreateBy(userId);
        relation.setCreateTime(now);
        relation.setDelFlag(0);
        relation.setDeleteTime(0L);
        bizIndustryProductRelMapper.insert(relation);
    }

    private void validateIndustry(Long industryId) {
        BizIndustry industry = bizIndustryMapper.selectById(industryId);
        if (industry == null) {
            throw new IllegalArgumentException("行业不存在: id=" + industryId);
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
}