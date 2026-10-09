package com.know.knowboot.service.biz.industry.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.biz.customer.BizCustomer;
import com.know.knowboot.entity.biz.customer.BizCustomerIndustry;
import com.know.knowboot.entity.biz.industry.BizEnterpriseProductRel;
import com.know.knowboot.entity.biz.industry.BizIndustry;
import com.know.knowboot.entity.biz.industry.BizIndustryEnterprise;
import com.know.knowboot.entity.biz.industry.BizIndustryEnterpriseRel;
import com.know.knowboot.entity.biz.industry.BizIndustryMarket;
import com.know.knowboot.entity.biz.industry.BizIndustryMarketRel;
import com.know.knowboot.entity.biz.industry.BizIndustryProduct;
import com.know.knowboot.entity.biz.industry.BizIndustryProductRel;
import com.know.knowboot.mapper.biz.customer.BizCustomerIndustryMapper;
import com.know.knowboot.mapper.biz.customer.BizCustomerMapper;
import com.know.knowboot.mapper.biz.industry.BizEnterpriseProductRelMapper;
import com.know.knowboot.mapper.biz.industry.BizIndustryEnterpriseMapper;
import com.know.knowboot.mapper.biz.industry.BizIndustryEnterpriseRelMapper;
import com.know.knowboot.mapper.biz.industry.BizIndustryMapper;
import com.know.knowboot.mapper.biz.industry.BizIndustryMarketMapper;
import com.know.knowboot.mapper.biz.industry.BizIndustryMarketRelMapper;
import com.know.knowboot.mapper.biz.industry.BizIndustryProductMapper;
import com.know.knowboot.mapper.biz.industry.BizIndustryProductRelMapper;
import com.know.knowboot.service.biz.industry.IBizIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 行业主体服务实现
 */
@Service
public class BizIndustryServiceImpl extends ServiceImpl<BizIndustryMapper, BizIndustry> implements IBizIndustryService {

    @Autowired
    private BizIndustryMapper bizIndustryMapper;

    @Autowired
    private BizIndustryProductMapper bizIndustryProductMapper;

    @Autowired
    private BizIndustryEnterpriseMapper bizIndustryEnterpriseMapper;

    @Autowired
    private BizIndustryMarketMapper bizIndustryMarketMapper;

    @Autowired
    private BizIndustryProductRelMapper bizIndustryProductRelMapper;

    @Autowired
    private BizIndustryEnterpriseRelMapper bizIndustryEnterpriseRelMapper;

    @Autowired
    private BizIndustryMarketRelMapper bizIndustryMarketRelMapper;

    @Autowired
    private BizEnterpriseProductRelMapper bizEnterpriseProductRelMapper;

    @Autowired
    private BizCustomerIndustryMapper bizCustomerIndustryMapper;

    @Autowired
    private BizCustomerMapper bizCustomerMapper;

    @Override
    public IPage<BizIndustry> page(BizIndustry query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<BizIndustry> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getIndustryName() != null, BizIndustry::getIndustryName, query.getIndustryName())
                .like(query.getIndustryCode() != null, BizIndustry::getIndustryCode, query.getIndustryCode())
                .orderByAsc(BizIndustry::getSort)
                .orderByDesc(BizIndustry::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public List<BizIndustry> listAll() {
        List<BizIndustry> list = list(new LambdaQueryWrapper<BizIndustry>()
                .orderByAsc(BizIndustry::getSort));
        fillParentNames(list);
        return list;
    }

    @Override
    public List<BizIndustry> tree(BizIndustry query) {
        List<BizIndustry> all = list(new LambdaQueryWrapper<BizIndustry>()
                .orderByAsc(BizIndustry::getSort));
        if (all == null || all.isEmpty()) {
            return all;
        }
        Map<Long, BizIndustry> byId = new HashMap<>();
        for (BizIndustry item : all) {
            byId.put(item.getId(), item);
        }
        // 关键字/可见性过滤：保留命中节点及其祖先链，保证树形连续
        boolean hasFilter = query != null
                && (hasText(query.getIndustryName()) || hasText(query.getIndustryCode())
                    || query.getVisibility() != null);
        Set<Long> keepIds = new HashSet<>();
        if (hasFilter) {
            Set<Long> hit = new HashSet<>();
            for (BizIndustry item : all) {
                if (match(query, item)) {
                    hit.add(item.getId());
                }
            }
            // 祖先链：命中节点的上级逐级保留（保证路径连续）
            for (Long id : hit) {
                Long pid = byId.get(id).getParentId();
                while (pid != null && byId.containsKey(pid)) {
                    keepIds.add(pid);
                    pid = byId.get(pid).getParentId();
                }
                keepIds.add(id);
            }
            // 后代：命中节点的下级逐级保留（保证子树可继续下钻）
            for (Long id : hit) {
                List<Long> childStack = new ArrayList<>();
                childStack.add(id);
                while (!childStack.isEmpty()) {
                    Long cur = childStack.remove(childStack.size() - 1);
                    for (BizIndustry item : all) {
                        if (cur.equals(item.getParentId()) && keepIds.add(item.getId())) {
                            childStack.add(item.getId());
                        }
                    }
                }
            }
        }
        // 组装 children 与上一级名称
        Map<Long, List<BizIndustry>> childrenMap = new HashMap<>();
        for (BizIndustry item : all) {
            if (hasFilter && !keepIds.contains(item.getId())) {
                continue;
            }
            if (item.getParentId() != null && byId.containsKey(item.getParentId())) {
                childrenMap.computeIfAbsent(item.getParentId(), k -> new ArrayList<>()).add(item);
            }
        }
        for (List<BizIndustry> kids : childrenMap.values()) {
            kids.sort(Comparator.comparing(BizIndustry::getSort,
                    Comparator.nullsLast(Comparator.naturalOrder())));
        }
        List<BizIndustry> roots = new ArrayList<>();
        for (BizIndustry item : all) {
            if (hasFilter && !keepIds.contains(item.getId())) {
                continue;
            }
            item.setChildren(childrenMap.getOrDefault(item.getId(), new ArrayList<>()));
            item.setParentName(buildParentName(byId, item));
            if (item.getParentId() == null || !byId.containsKey(item.getParentId())) {
                roots.add(item);
            }
        }
        return roots;
    }

    /** 填充扁平列表的上级行业名称（下拉框展示用） */
    private void fillParentNames(List<BizIndustry> list) {
        if (list == null) {
            return;
        }
        Map<Long, BizIndustry> byId = new HashMap<>();
        for (BizIndustry item : list) {
            byId.put(item.getId(), item);
        }
        for (BizIndustry item : list) {
            item.setParentName(buildParentName(byId, item));
        }
    }

    private String buildParentName(Map<Long, BizIndustry> byId, BizIndustry item) {
        if (item.getParentId() == null) {
            return null;
        }
        BizIndustry parent = byId.get(item.getParentId());
        return parent == null ? null : parent.getIndustryName();
    }

    private boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }

    /** parentId 为空字符串/0 视为顶级行业 */
    private void normalizeParentId(BizIndustry industry) {
        if (industry.getParentId() != null && industry.getParentId() == 0L) {
            industry.setParentId(null);
        }
    }

    /** 上级行业不能是自己，也不能是自己的下级 */
    private void validateHierarchy(Long id, Long parentId) {
        if (id == null || parentId == null) {
            return;
        }
        if (parentId.equals(id)) {
            throw new IllegalArgumentException("上级行业不能是自己");
        }
        List<BizIndustry> all = list(new LambdaQueryWrapper<BizIndustry>()
                .eq(BizIndustry::getDelFlag, 0));
        Map<Long, List<Long>> childrenMap = new HashMap<>();
        for (BizIndustry item : all) {
            if (item.getParentId() != null) {
                childrenMap.computeIfAbsent(item.getParentId(), k -> new ArrayList<>()).add(item.getId());
            }
        }
        Set<Long> visited = new HashSet<>();
        Deque<Long> stack = new ArrayDeque<>(childrenMap.getOrDefault(id, new ArrayList<>()));
        while (!stack.isEmpty()) {
            Long cur = stack.pop();
            if (cur.equals(parentId)) {
                throw new IllegalArgumentException("上级行业不能是自己的下级行业");
            }
            if (visited.add(cur)) {
                List<Long> kids = childrenMap.get(cur);
                if (kids != null) {
                    stack.addAll(kids);
                }
            }
        }
    }

    private boolean match(BizIndustry query, BizIndustry item) {
        if (query.getIndustryName() != null && !query.getIndustryName().trim().isEmpty()
                && (item.getIndustryName() == null
                    || !item.getIndustryName().contains(query.getIndustryName().trim()))) {
            return false;
        }
        if (query.getIndustryCode() != null && !query.getIndustryCode().trim().isEmpty()
                && (item.getIndustryCode() == null
                    || !item.getIndustryCode().contains(query.getIndustryCode().trim()))) {
            return false;
        }
        if (query.getVisibility() != null && !query.getVisibility().equals(item.getVisibility())) {
            return false;
        }
        return true;
    }

    @Override
    public BizIndustry getDetail(Long id) {
        BizIndustry industry = bizIndustryMapper.selectById(id);
        if (industry == null) {
            return null;
        }
        if (industry.getParentId() != null) {
            BizIndustry parent = bizIndustryMapper.selectById(industry.getParentId());
            if (parent != null) {
                industry.setParentName(parent.getIndustryName());
            }
        }
        List<Long> marketIds = getRelMarketIds(id);
        if (!marketIds.isEmpty()) {
            List<BizIndustryMarket> markets = bizIndustryMarketMapper.selectBatchIds(marketIds);
            if (markets != null && !markets.isEmpty()) {
                industry.setMarket(markets.get(0));
            }
        }
        List<Long> productIds = getRelProductIds(id);
        if (!productIds.isEmpty()) {
            industry.setProducts(bizIndustryProductMapper.selectBatchIds(productIds));
        }
        List<Long> enterpriseIds = getRelEnterpriseIds(id);
        if (!enterpriseIds.isEmpty()) {
            industry.setEnterprises(bizIndustryEnterpriseMapper.selectBatchIds(enterpriseIds));
        }
        Long customerCount = bizCustomerIndustryMapper.selectCount(new LambdaQueryWrapper<BizCustomerIndustry>()
                .eq(BizCustomerIndustry::getIndustryId, id)
                .eq(BizCustomerIndustry::getDelFlag, 0));
        industry.setCustomerCount(customerCount);
        return industry;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(BizIndustry industry, Long userId) {
        normalizeParentId(industry);
        long now = System.currentTimeMillis();
        industry.setCreateBy(userId);
        industry.setCreateTime(now);
        industry.setDelFlag(0);
        industry.setDeleteTime(0L);
        bizIndustryMapper.insert(industry);
        Long industryId = industry.getId();
        if (industry.getMarket() != null) {
            BizIndustryMarket market = industry.getMarket();
            market.setId(null);
            market.setCreateBy(userId);
            market.setCreateTime(now);
            market.setDelFlag(0);
            market.setDeleteTime(0L);
            bizIndustryMarketMapper.insert(market);
            insertMarketRel(industryId, market.getId(), userId, now);
        }
        if (industry.getProducts() != null && !industry.getProducts().isEmpty()) {
            for (BizIndustryProduct product : industry.getProducts()) {
                product.setId(null);
                product.setCreateBy(userId);
                product.setCreateTime(now);
                product.setDelFlag(0);
                product.setDeleteTime(0L);
                bizIndustryProductMapper.insert(product);
                insertProductRel(industryId, product.getId(), userId, now);
            }
        }
        if (industry.getEnterprises() != null && !industry.getEnterprises().isEmpty()) {
            for (BizIndustryEnterprise enterprise : industry.getEnterprises()) {
                enterprise.setId(null);
                enterprise.setCreateBy(userId);
                enterprise.setCreateTime(now);
                enterprise.setDelFlag(0);
                enterprise.setDeleteTime(0L);
                bizIndustryEnterpriseMapper.insert(enterprise);
                insertEnterpriseRel(industryId, enterprise.getId(), userId, now);
            }
        }
        return industryId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(BizIndustry industry, Long userId) {
        normalizeParentId(industry);
        validateHierarchy(industry.getId(), industry.getParentId());
        industry.setUpdateBy(userId);
        industry.setUpdateTime(System.currentTimeMillis());
        updateById(industry);
        if (industry.getMarket() != null) {
            BizIndustryMarket market = industry.getMarket();
            long now = System.currentTimeMillis();
            if (market.getId() != null) {
                BizIndustryMarket existing = bizIndustryMarketMapper.selectById(market.getId());
                if (existing != null) {
                    market.setUpdateBy(userId);
                    market.setUpdateTime(now);
                    bizIndustryMarketMapper.updateById(market);
                } else {
                    market.setId(null);
                    market.setCreateBy(userId);
                    market.setCreateTime(now);
                    market.setDelFlag(0);
                    market.setDeleteTime(0L);
                    bizIndustryMarketMapper.insert(market);
                    insertMarketRel(industry.getId(), market.getId(), userId, now);
                }
            } else {
                List<Long> marketIds = getRelMarketIds(industry.getId());
                if (marketIds.isEmpty()) {
                    market.setId(null);
                    market.setCreateBy(userId);
                    market.setCreateTime(now);
                    market.setDelFlag(0);
                    market.setDeleteTime(0L);
                    bizIndustryMarketMapper.insert(market);
                    insertMarketRel(industry.getId(), market.getId(), userId, now);
                } else {
                    BizIndustryMarket existing = bizIndustryMarketMapper.selectById(marketIds.get(0));
                    if (existing != null) {
                        market.setId(existing.getId());
                        market.setUpdateBy(userId);
                        market.setUpdateTime(now);
                        bizIndustryMarketMapper.updateById(market);
                    } else {
                        market.setId(null);
                        market.setCreateBy(userId);
                        market.setCreateTime(now);
                        market.setDelFlag(0);
                        market.setDeleteTime(0L);
                        bizIndustryMarketMapper.insert(market);
                        insertMarketRel(industry.getId(), market.getId(), userId, now);
                    }
                }
            }
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        // 存在下级行业时不允许删除
        Long childCount = bizIndustryMapper.selectCount(new LambdaQueryWrapper<BizIndustry>()
                .eq(BizIndustry::getParentId, id)
                .eq(BizIndustry::getDelFlag, 0));
        if (childCount != null && childCount > 0) {
            throw new IllegalArgumentException("存在下级行业，请先删除下级行业");
        }
        removeById(id);
        long deleteTime = System.currentTimeMillis();
        softDeleteRel(bizIndustryProductRelMapper, new LambdaUpdateWrapper<BizIndustryProductRel>()
                .eq(BizIndustryProductRel::getIndustryId, id)
                .set(BizIndustryProductRel::getDelFlag, 1)
                .set(BizIndustryProductRel::getDeleteTime, deleteTime));
        List<Long> enterpriseIds = getRelEnterpriseIds(id);
        softDeleteRel(bizIndustryEnterpriseRelMapper, new LambdaUpdateWrapper<BizIndustryEnterpriseRel>()
                .eq(BizIndustryEnterpriseRel::getIndustryId, id)
                .set(BizIndustryEnterpriseRel::getDelFlag, 1)
                .set(BizIndustryEnterpriseRel::getDeleteTime, deleteTime));
        softDeleteRel(bizIndustryMarketRelMapper, new LambdaUpdateWrapper<BizIndustryMarketRel>()
                .eq(BizIndustryMarketRel::getIndustryId, id)
                .set(BizIndustryMarketRel::getDelFlag, 1)
                .set(BizIndustryMarketRel::getDeleteTime, deleteTime));
        if (!enterpriseIds.isEmpty()) {
            softDeleteRel(bizEnterpriseProductRelMapper, new LambdaUpdateWrapper<BizEnterpriseProductRel>()
                    .in(BizEnterpriseProductRel::getEnterpriseId, enterpriseIds)
                    .set(BizEnterpriseProductRel::getDelFlag, 1)
                    .set(BizEnterpriseProductRel::getDeleteTime, deleteTime));
        }
        return Boolean.TRUE;
    }

    @Override
    public IPage<Map<String, Object>> getCustomers(Long industryId, String keyword, Integer pageNum, Integer pageSize) {
        Page<BizCustomerIndustry> relPage = bizCustomerIndustryMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<BizCustomerIndustry>()
                        .eq(BizCustomerIndustry::getIndustryId, industryId)
                        .eq(BizCustomerIndustry::getDelFlag, 0));
        List<Long> customerIds = new ArrayList<>();
        for (BizCustomerIndustry rel : relPage.getRecords()) {
            customerIds.add(rel.getCustomerId());
        }
        Map<Long, BizCustomer> customerMap = new HashMap<>();
        if (!customerIds.isEmpty()) {
            for (BizCustomer customer : bizCustomerMapper.selectBatchIds(customerIds)) {
                customerMap.put(customer.getId(), customer);
            }
        }
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (BizCustomerIndustry rel : relPage.getRecords()) {
            BizCustomer customer = customerMap.get(rel.getCustomerId());
            if (customer == null) {
                continue;
            }
            if (keyword != null && (customer.getName() == null || !customer.getName().contains(keyword))) {
                continue;
            }
            Map<String, Object> item = new HashMap<>();
            item.put("id", customer.getId());
            item.put("name", customer.getName());
            item.put("customerType", customer.getCustomerType());
            item.put("status", customer.getStatus());
            item.put("source", customer.getSource());
            item.put("phone", customer.getPhone());
            item.put("relationType", rel.getRelationType());
            item.put("isMain", rel.getIsMain());
            item.put("remark", rel.getRemark());
            resultList.add(item);
        }
        Page<Map<String, Object>> result = new Page<>(pageNum, pageSize);
        result.setRecords(resultList);
        result.setTotal(resultList.size());
        return result;
    }

    @Override
    public Map<String, Object> statistics() {
        Map<String, Object> result = new HashMap<>();
        result.put("industryCount", bizIndustryMapper.selectCount(new LambdaQueryWrapper<BizIndustry>()
                .eq(BizIndustry::getDelFlag, 0)));
        result.put("productCount", bizIndustryProductMapper.selectCount(new LambdaQueryWrapper<BizIndustryProduct>()
                .eq(BizIndustryProduct::getDelFlag, 0)));
        result.put("enterpriseCount", bizIndustryEnterpriseMapper.selectCount(new LambdaQueryWrapper<BizIndustryEnterprise>()
                .eq(BizIndustryEnterprise::getDelFlag, 0)));
        result.put("relationCount", bizCustomerIndustryMapper.selectCount(new LambdaQueryWrapper<BizCustomerIndustry>()
                .eq(BizCustomerIndustry::getDelFlag, 0)));
        return result;
    }

    private List<Long> getRelMarketIds(Long industryId) {
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

    private List<Long> getRelProductIds(Long industryId) {
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

    private List<Long> getRelEnterpriseIds(Long industryId) {
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

    private void insertMarketRel(Long industryId, Long marketId, Long userId, long now) {
        BizIndustryMarketRel relation = new BizIndustryMarketRel();
        relation.setIndustryId(industryId);
        relation.setMarketId(marketId);
        relation.setCreateBy(userId);
        relation.setCreateTime(now);
        relation.setDelFlag(0);
        relation.setDeleteTime(0L);
        bizIndustryMarketRelMapper.insert(relation);
    }

    private void insertProductRel(Long industryId, Long productId, Long userId, long now) {
        BizIndustryProductRel relation = new BizIndustryProductRel();
        relation.setIndustryId(industryId);
        relation.setProductId(productId);
        relation.setCreateBy(userId);
        relation.setCreateTime(now);
        relation.setDelFlag(0);
        relation.setDeleteTime(0L);
        bizIndustryProductRelMapper.insert(relation);
    }

    private void insertEnterpriseRel(Long industryId, Long enterpriseId, Long userId, long now) {
        BizIndustryEnterpriseRel relation = new BizIndustryEnterpriseRel();
        relation.setIndustryId(industryId);
        relation.setEnterpriseId(enterpriseId);
        relation.setCreateBy(userId);
        relation.setCreateTime(now);
        relation.setDelFlag(0);
        relation.setDeleteTime(0L);
        bizIndustryEnterpriseRelMapper.insert(relation);
    }

    private <T> void softDeleteRel(com.baomidou.mybatisplus.core.mapper.BaseMapper<T> mapper, LambdaUpdateWrapper<T> wrapper) {
        mapper.update(null, wrapper);
    }
}