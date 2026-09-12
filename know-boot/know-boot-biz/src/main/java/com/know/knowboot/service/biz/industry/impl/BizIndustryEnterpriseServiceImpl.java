package com.know.knowboot.service.biz.industry.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.biz.industry.BizEnterpriseProductRel;
import com.know.knowboot.entity.biz.industry.BizIndustry;
import com.know.knowboot.entity.biz.industry.BizIndustryEnterprise;
import com.know.knowboot.entity.biz.industry.BizIndustryEnterpriseRel;
import com.know.knowboot.entity.biz.industry.BizIndustryProduct;
import com.know.knowboot.mapper.biz.industry.BizEnterpriseProductRelMapper;
import com.know.knowboot.mapper.biz.industry.BizIndustryEnterpriseMapper;
import com.know.knowboot.mapper.biz.industry.BizIndustryEnterpriseRelMapper;
import com.know.knowboot.mapper.biz.industry.BizIndustryMapper;
import com.know.knowboot.mapper.biz.industry.BizIndustryProductMapper;
import com.know.knowboot.service.biz.industry.IBizIndustryEnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 行业企业服务实现
 */
@Service
public class BizIndustryEnterpriseServiceImpl extends ServiceImpl<BizIndustryEnterpriseMapper, BizIndustryEnterprise> implements IBizIndustryEnterpriseService {

    @Autowired
    private BizIndustryEnterpriseMapper bizIndustryEnterpriseMapper;

    @Autowired
    private BizIndustryEnterpriseRelMapper bizIndustryEnterpriseRelMapper;

    @Autowired
    private BizEnterpriseProductRelMapper bizEnterpriseProductRelMapper;

    @Autowired
    private BizIndustryMapper bizIndustryMapper;

    @Autowired
    private BizIndustryProductMapper bizIndustryProductMapper;

    @Override
    public IPage<BizIndustryEnterprise> page(Long industryId, String enterpriseType, String enterpriseName, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<BizIndustryEnterprise> wrapper = new LambdaQueryWrapper<>();
        if (industryId != null) {
            List<Long> enterpriseIds = getEnterpriseIdsByIndustry(industryId);
            if (enterpriseIds.isEmpty()) {
                return new Page<>(pageNum, pageSize);
            }
            wrapper.in(BizIndustryEnterprise::getId, enterpriseIds);
        }
        wrapper.like(enterpriseType != null, BizIndustryEnterprise::getEnterpriseType, enterpriseType)
                .like(enterpriseName != null, BizIndustryEnterprise::getEnterpriseName, enterpriseName)
                .orderByDesc(BizIndustryEnterprise::getCreateTime);
        IPage<BizIndustryEnterprise> result = page(new Page<>(pageNum, pageSize), wrapper);
        fillRelations(result.getRecords());
        return result;
    }

    // 分页结果填充关联行业/产品(避免列表页无关联信息)
    private void fillRelations(List<BizIndustryEnterprise> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        List<Long> enterpriseIds = new ArrayList<>();
        for (BizIndustryEnterprise record : records) {
            enterpriseIds.add(record.getId());
        }
        List<BizIndustryEnterpriseRel> industryRels = bizIndustryEnterpriseRelMapper.selectList(new LambdaQueryWrapper<BizIndustryEnterpriseRel>()
                .in(BizIndustryEnterpriseRel::getEnterpriseId, enterpriseIds)
                .eq(BizIndustryEnterpriseRel::getDelFlag, 0));
        Map<Long, List<Long>> enterpriseIndustryMap = new HashMap<>();
        if (industryRels != null) {
            for (BizIndustryEnterpriseRel rel : industryRels) {
                enterpriseIndustryMap.computeIfAbsent(rel.getEnterpriseId(), k -> new ArrayList<>()).add(rel.getIndustryId());
            }
        }
        List<BizEnterpriseProductRel> productRels = bizEnterpriseProductRelMapper.selectList(new LambdaQueryWrapper<BizEnterpriseProductRel>()
                .in(BizEnterpriseProductRel::getEnterpriseId, enterpriseIds)
                .eq(BizEnterpriseProductRel::getDelFlag, 0));
        Map<Long, List<Long>> enterpriseProductMap = new HashMap<>();
        if (productRels != null) {
            for (BizEnterpriseProductRel rel : productRels) {
                enterpriseProductMap.computeIfAbsent(rel.getEnterpriseId(), k -> new ArrayList<>()).add(rel.getProductId());
            }
        }
        for (BizIndustryEnterprise record : records) {
            List<Long> industryIds = enterpriseIndustryMap.getOrDefault(record.getId(), new ArrayList<>());
            record.setIndustryIds(industryIds);
            record.setIndustryNames(queryIndustryNames(industryIds));
            List<Long> productIds = enterpriseProductMap.getOrDefault(record.getId(), new ArrayList<>());
            record.setProductIds(productIds);
            record.setProductNames(queryProductNames(productIds));
        }
    }

    @Override
    public BizIndustryEnterprise getDetail(Long id) {
        BizIndustryEnterprise enterprise = bizIndustryEnterpriseMapper.selectById(id);
        if (enterprise == null) {
            return null;
        }
        List<BizIndustryEnterpriseRel> industryRelations = bizIndustryEnterpriseRelMapper.selectList(new LambdaQueryWrapper<BizIndustryEnterpriseRel>()
                .eq(BizIndustryEnterpriseRel::getEnterpriseId, id)
                .eq(BizIndustryEnterpriseRel::getDelFlag, 0));
        List<Long> industryIds = new ArrayList<>();
        if (industryRelations != null) {
            for (BizIndustryEnterpriseRel relation : industryRelations) {
                industryIds.add(relation.getIndustryId());
            }
        }
        enterprise.setIndustryIds(industryIds);
        enterprise.setIndustryNames(queryIndustryNames(industryIds));
        List<BizEnterpriseProductRel> productRelations = bizEnterpriseProductRelMapper.selectList(new LambdaQueryWrapper<BizEnterpriseProductRel>()
                .eq(BizEnterpriseProductRel::getEnterpriseId, id)
                .eq(BizEnterpriseProductRel::getDelFlag, 0));
        List<Long> productIds = new ArrayList<>();
        if (productRelations != null) {
            for (BizEnterpriseProductRel relation : productRelations) {
                productIds.add(relation.getProductId());
            }
        }
        enterprise.setProductIds(productIds);
        enterprise.setProductNames(queryProductNames(productIds));
        return enterprise;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(BizIndustryEnterprise enterprise, Long userId) {
        long now = System.currentTimeMillis();
        enterprise.setCreateBy(userId);
        enterprise.setCreateTime(now);
        enterprise.setDelFlag(0);
        enterprise.setDeleteTime(0L);
        save(enterprise);
        if (enterprise.getIndustryIds() != null && !enterprise.getIndustryIds().isEmpty()) {
            for (Long industryId : enterprise.getIndustryIds()) {
                validateIndustry(industryId);
                insertIndustryRel(enterprise.getId(), industryId, userId, now);
            }
        }
        if (enterprise.getProductIds() != null && !enterprise.getProductIds().isEmpty()) {
            for (Long productId : enterprise.getProductIds()) {
                validateProduct(productId);
                insertProductRel(enterprise.getId(), productId, userId, now);
            }
        }
        return enterprise.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(BizIndustryEnterprise enterprise, Long userId) {
        enterprise.setUpdateBy(userId);
        enterprise.setUpdateTime(System.currentTimeMillis());
        updateById(enterprise);
        if (enterprise.getIndustryIds() != null) {
            replaceIndustries(enterprise.getId(), enterprise.getIndustryIds(), userId);
        }
        if (enterprise.getProductIds() != null) {
            replaceProducts(enterprise.getId(), enterprise.getProductIds(), userId);
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        removeById(id);
        long now = System.currentTimeMillis();
        bizIndustryEnterpriseRelMapper.update(null, new LambdaUpdateWrapper<BizIndustryEnterpriseRel>()
                .eq(BizIndustryEnterpriseRel::getEnterpriseId, id)
                .set(BizIndustryEnterpriseRel::getDelFlag, 1)
                .set(BizIndustryEnterpriseRel::getDeleteTime, now));
        bizEnterpriseProductRelMapper.update(null, new LambdaUpdateWrapper<BizEnterpriseProductRel>()
                .eq(BizEnterpriseProductRel::getEnterpriseId, id)
                .set(BizEnterpriseProductRel::getDelFlag, 1)
                .set(BizEnterpriseProductRel::getDeleteTime, now));
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean setIndustries(Long id, List<Long> industryIds, Long userId) {
        replaceIndustries(id, industryIds, userId);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean setProducts(Long id, List<Long> productIds, Long userId) {
        replaceProducts(id, productIds, userId);
        return Boolean.TRUE;
    }

    private List<Long> getEnterpriseIdsByIndustry(Long industryId) {
        List<BizIndustryEnterpriseRel> relations = bizIndustryEnterpriseRelMapper.selectList(new LambdaQueryWrapper<BizIndustryEnterpriseRel>()
                .eq(BizIndustryEnterpriseRel::getIndustryId, industryId)
                .eq(BizIndustryEnterpriseRel::getDelFlag, 0));
        List<Long> enterpriseIds = new ArrayList<>();
        if (relations != null) {
            for (BizIndustryEnterpriseRel relation : relations) {
                enterpriseIds.add(relation.getEnterpriseId());
            }
        }
        return enterpriseIds;
    }

    private void replaceIndustries(Long enterpriseId, List<Long> industryIds, Long userId) {
        long now = System.currentTimeMillis();
        if (industryIds != null) {
            for (Long industryId : industryIds) {
                validateIndustry(industryId);
            }
        }
        // 物理删除旧关联(含逻辑删除残留),避免唯一索引冲突
        bizIndustryEnterpriseRelMapper.hardDeleteByEnterpriseId(enterpriseId);
        if (industryIds != null) {
            for (Long industryId : industryIds) {
                insertIndustryRel(enterpriseId, industryId, userId, now);
            }
        }
    }

    private void replaceProducts(Long enterpriseId, List<Long> productIds, Long userId) {
        long now = System.currentTimeMillis();
        if (productIds != null) {
            for (Long productId : productIds) {
                validateProduct(productId);
            }
        }
        // 物理删除旧关联(含逻辑删除残留),避免唯一索引冲突
        bizEnterpriseProductRelMapper.hardDeleteByEnterpriseId(enterpriseId);
        if (productIds != null) {
            for (Long productId : productIds) {
                insertProductRel(enterpriseId, productId, userId, now);
            }
        }
    }

    private void insertIndustryRel(Long enterpriseId, Long industryId, Long userId, long now) {
        BizIndustryEnterpriseRel relation = new BizIndustryEnterpriseRel();
        relation.setIndustryId(industryId);
        relation.setEnterpriseId(enterpriseId);
        relation.setCreateBy(userId);
        relation.setCreateTime(now);
        relation.setDelFlag(0);
        relation.setDeleteTime(0L);
        bizIndustryEnterpriseRelMapper.insert(relation);
    }

    private void insertProductRel(Long enterpriseId, Long productId, Long userId, long now) {
        BizEnterpriseProductRel relation = new BizEnterpriseProductRel();
        relation.setEnterpriseId(enterpriseId);
        relation.setProductId(productId);
        relation.setCreateBy(userId);
        relation.setCreateTime(now);
        relation.setDelFlag(0);
        relation.setDeleteTime(0L);
        bizEnterpriseProductRelMapper.insert(relation);
    }

    private void validateIndustry(Long industryId) {
        BizIndustry industry = bizIndustryMapper.selectById(industryId);
        if (industry == null) {
            throw new IllegalArgumentException("行业不存在: id=" + industryId);
        }
    }

    private void validateProduct(Long productId) {
        BizIndustryProduct product = bizIndustryProductMapper.selectById(productId);
        if (product == null) {
            throw new IllegalArgumentException("产品不存在: id=" + productId);
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

    private List<String> queryProductNames(List<Long> productIds) {
        List<String> productNames = new ArrayList<>();
        if (productIds == null || productIds.isEmpty()) {
            return productNames;
        }
        Map<Long, String> nameMap = new HashMap<>();
        List<BizIndustryProduct> productList = bizIndustryProductMapper.selectBatchIds(productIds);
        if (productList != null) {
            for (BizIndustryProduct product : productList) {
                nameMap.put(product.getId(), product.getProductName());
            }
        }
        for (Long productId : productIds) {
            productNames.add(nameMap.get(productId));
        }
        return productNames;
    }
}