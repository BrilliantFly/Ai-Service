package com.know.knowboot.service.biz.customer.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.know.knowboot.entity.biz.customer.BizCustomer;
import com.know.knowboot.entity.biz.customer.BizCustomerCompany;
import com.know.knowboot.entity.biz.customer.BizCustomerFollowup;
import com.know.knowboot.entity.biz.customer.BizCustomerIndustry;
import com.know.knowboot.entity.biz.customer.BizCustomerProfile;
import com.know.knowboot.entity.biz.industry.BizIndustry;
import com.know.knowboot.mapper.biz.customer.BizCustomerCompanyMapper;
import com.know.knowboot.mapper.biz.customer.BizCustomerFollowupMapper;
import com.know.knowboot.mapper.biz.customer.BizCustomerIndustryMapper;
import com.know.knowboot.mapper.biz.customer.BizCustomerMapper;
import com.know.knowboot.mapper.biz.customer.BizCustomerProfileMapper;
import com.know.knowboot.mapper.biz.industry.BizIndustryMapper;
import com.know.knowboot.service.biz.customer.IBizCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户服务实现
 */
@Service
public class BizCustomerServiceImpl extends ServiceImpl<BizCustomerMapper, BizCustomer> implements IBizCustomerService {

    @Autowired
    private BizCustomerMapper bizCustomerMapper;

    @Autowired
    private BizCustomerProfileMapper bizCustomerProfileMapper;

    @Autowired
    private BizCustomerCompanyMapper bizCustomerCompanyMapper;

    @Autowired
    private BizCustomerFollowupMapper bizCustomerFollowupMapper;

    @Autowired
    private BizCustomerIndustryMapper bizCustomerIndustryMapper;

    @Autowired
    private BizIndustryMapper bizIndustryMapper;

    @Override
    public IPage<BizCustomer> page(BizCustomer query, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<BizCustomer> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(query.getName() != null, BizCustomer::getName, query.getName())
                .like(query.getPhone() != null, BizCustomer::getPhone, query.getPhone())
                .eq(query.getCustomerType() != null, BizCustomer::getCustomerType, query.getCustomerType())
                .eq(query.getStatus() != null, BizCustomer::getStatus, query.getStatus())
                .eq(query.getCompanyId() != null, BizCustomer::getCompanyId, query.getCompanyId())
                .orderByDesc(BizCustomer::getCreateTime);
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public BizCustomer getDetail(Long id) {
        BizCustomer customer = bizCustomerMapper.selectById(id);
        if (customer == null) {
            return null;
        }
        BizCustomerProfile profile = bizCustomerProfileMapper.selectOne(new LambdaQueryWrapper<BizCustomerProfile>()
                .eq(BizCustomerProfile::getCustomerId, id)
                .last("LIMIT 1"));
        customer.setProfile(profile);
        BizCustomerCompany company = null;
        if (customer.getCompanyId() != null) {
            company = bizCustomerCompanyMapper.selectById(customer.getCompanyId());
        }
        customer.setCompany(company);
        List<BizCustomerIndustry> industries = bizCustomerIndustryMapper.selectList(new LambdaQueryWrapper<BizCustomerIndustry>()
                .eq(BizCustomerIndustry::getCustomerId, id)
                .orderByDesc(BizCustomerIndustry::getIsMain));
        if (industries != null && !industries.isEmpty()) {
            List<Long> industryIds = new ArrayList<>();
            for (BizCustomerIndustry relation : industries) {
                industryIds.add(relation.getIndustryId());
            }
            List<BizIndustry> industryList = bizIndustryMapper.selectBatchIds(industryIds);
            Map<Long, String> nameMap = new HashMap<>();
            if (industryList != null) {
                for (BizIndustry industry : industryList) {
                    nameMap.put(industry.getId(), industry.getIndustryName());
                }
            }
            for (BizCustomerIndustry relation : industries) {
                relation.setIndustryName(nameMap.get(relation.getIndustryId()));
            }
        }
        customer.setIndustries(industries);
        return customer;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(BizCustomer customer, Long userId) {
        long now = System.currentTimeMillis();
        customer.setCreateBy(userId);
        customer.setCreateTime(now);
        customer.setDeleted(0);
        customer.setDeleteTime(0L);
        save(customer);
        Long customerId = customer.getId();
        if (customer.getProfile() != null) {
            BizCustomerProfile profile = customer.getProfile();
            profile.setId(null);
            profile.setCustomerId(customerId);
            profile.setCreateBy(userId);
            profile.setCreateTime(now);
            profile.setDelFlag(0);
            profile.setDeleteTime(0L);
            bizCustomerProfileMapper.insert(profile);
        }
        if (customer.getCompany() != null) {
            BizCustomerCompany company = customer.getCompany();
            company.setId(null);
            company.setCreateBy(userId);
            company.setCreateTime(now);
            company.setDeleted(0);
            company.setDeleteTime(0L);
            bizCustomerCompanyMapper.insert(company);
            customer.setCompanyId(company.getId());
        }
        if (customer.getIndustryIds() != null && !customer.getIndustryIds().isEmpty()) {
            boolean mainSet = false;
            for (Long industryId : customer.getIndustryIds()) {
                BizIndustry industry = bizIndustryMapper.selectById(industryId);
                if (industry == null) {
                    throw new IllegalArgumentException("行业不存在: id=" + industryId);
                }
                BizCustomerIndustry relation = new BizCustomerIndustry();
                relation.setCustomerId(customerId);
                relation.setIndustryId(industryId);
                relation.setRelationType(1);
                relation.setIsMain(mainSet ? 0 : 1);
                mainSet = true;
                relation.setCreateBy(userId);
                relation.setCreateTime(now);
                relation.setDelFlag(0);
                relation.setDeleteTime(0L);
                bizCustomerIndustryMapper.insert(relation);
            }
        }
        return customerId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(BizCustomer customer, Long userId) {
        customer.setUpdateBy(userId);
        customer.setUpdateTime(System.currentTimeMillis());
        updateById(customer);
        if (customer.getProfile() != null) {
            BizCustomerProfile profile = customer.getProfile();
            profile.setCustomerId(customer.getId());
            BizCustomerProfile existing = bizCustomerProfileMapper.selectOne(new LambdaQueryWrapper<BizCustomerProfile>()
                    .eq(BizCustomerProfile::getCustomerId, customer.getId())
                    .last("LIMIT 1"));
            if (existing != null) {
                profile.setId(existing.getId());
                profile.setUpdateBy(userId);
                profile.setUpdateTime(System.currentTimeMillis());
                bizCustomerProfileMapper.updateById(profile);
            } else {
                profile.setId(null);
                profile.setCreateBy(userId);
                profile.setCreateTime(System.currentTimeMillis());
                profile.setDelFlag(0);
                profile.setDeleteTime(0L);
                bizCustomerProfileMapper.insert(profile);
            }
        }
        if (customer.getCompany() != null) {
            BizCustomerCompany company = customer.getCompany();
            BizCustomerCompany existing = null;
            if (customer.getCompanyId() != null) {
                existing = bizCustomerCompanyMapper.selectById(customer.getCompanyId());
            }
            if (existing != null) {
                company.setId(existing.getId());
                company.setUpdateBy(userId);
                company.setUpdateTime(System.currentTimeMillis());
                bizCustomerCompanyMapper.updateById(company);
            } else {
                company.setId(null);
                company.setCreateBy(userId);
                company.setCreateTime(System.currentTimeMillis());
                company.setDeleted(0);
                company.setDeleteTime(0L);
                bizCustomerCompanyMapper.insert(company);
                customer.setCompanyId(company.getId());
                bizCustomerMapper.update(null, new LambdaUpdateWrapper<BizCustomer>()
                        .eq(BizCustomer::getId, customer.getId())
                        .set(BizCustomer::getCompanyId, company.getId()));
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Long id) {
        BizCustomer existing = bizCustomerMapper.selectById(id);
        removeById(id);
        long now = System.currentTimeMillis();
        bizCustomerProfileMapper.update(null, new LambdaUpdateWrapper<BizCustomerProfile>()
                .eq(BizCustomerProfile::getCustomerId, id)
                .set(BizCustomerProfile::getDelFlag, 1)
                .set(BizCustomerProfile::getDeleteTime, now));
        if (existing != null && existing.getCompanyId() != null) {
            bizCustomerCompanyMapper.update(null, new LambdaUpdateWrapper<BizCustomerCompany>()
                    .eq(BizCustomerCompany::getId, existing.getCompanyId())
                    .set(BizCustomerCompany::getDeleted, 1)
                    .set(BizCustomerCompany::getDeleteTime, now));
        }
        bizCustomerFollowupMapper.delete(new LambdaUpdateWrapper<BizCustomerFollowup>()
                .eq(BizCustomerFollowup::getCustomerId, id));
        bizCustomerIndustryMapper.update(null, new LambdaUpdateWrapper<BizCustomerIndustry>()
                .eq(BizCustomerIndustry::getCustomerId, id)
                .set(BizCustomerIndustry::getDelFlag, 1)
                .set(BizCustomerIndustry::getDeleteTime, now));
        return true;
    }

    @Override
    public Boolean updateStatus(Long id, Integer status, Long userId) {
        BizCustomer customer = new BizCustomer();
        customer.setId(id);
        customer.setStatus(status);
        customer.setUpdateBy(userId);
        customer.setUpdateTime(System.currentTimeMillis());
        return updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean setIndustries(Long id, List<BizCustomerIndustry> relations, Long userId) {
        if (relations == null || relations.isEmpty()) {
            return false;
        }
        int mainCount = 0;
        for (BizCustomerIndustry relation : relations) {
            if (relation.getIndustryId() == null) {
                throw new IllegalArgumentException("行业ID不能为空");
            }
            BizIndustry industry = bizIndustryMapper.selectById(relation.getIndustryId());
            if (industry == null) {
                throw new IllegalArgumentException("行业不存在: id=" + relation.getIndustryId());
            }
            if (relation.getIsMain() != null && relation.getIsMain() == 1) {
                mainCount++;
            }
        }
        if (mainCount > 1) {
            throw new IllegalArgumentException("主营行业最多只能设置一个");
        }
        long now = System.currentTimeMillis();
        bizCustomerIndustryMapper.update(null, new LambdaUpdateWrapper<BizCustomerIndustry>()
                .eq(BizCustomerIndustry::getCustomerId, id)
                .set(BizCustomerIndustry::getDelFlag, 1)
                .set(BizCustomerIndustry::getDeleteTime, now));
        for (BizCustomerIndustry relation : relations) {
            relation.setId(null);
            relation.setCustomerId(id);
            if (relation.getRelationType() == null) {
                relation.setRelationType(1);
            }
            if (relation.getIsMain() == null) {
                relation.setIsMain(0);
            }
            relation.setCreateBy(userId);
            relation.setCreateTime(now);
            relation.setDelFlag(0);
            relation.setDeleteTime(0L);
            bizCustomerIndustryMapper.insert(relation);
        }
        return true;
    }

    @Override
    public Map<String, Object> statistics() {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> industryRows = bizCustomerIndustryMapper.selectMaps(
                new QueryWrapper<BizCustomerIndustry>()
                        .select("industry_id AS industryId", "COUNT(*) AS cnt")
                        .groupBy("industry_id"));
        List<Map<String, Object>> industryDistribution = new ArrayList<>();
        if (industryRows != null && !industryRows.isEmpty()) {
            List<Long> industryIds = new ArrayList<>();
            for (Map<String, Object> row : industryRows) {
                industryIds.add(mapLong(row, "industryId"));
            }
            Map<Long, String> nameMap = new HashMap<>();
            List<BizIndustry> industryList = bizIndustryMapper.selectBatchIds(industryIds);
            if (industryList != null) {
                for (BizIndustry industry : industryList) {
                    nameMap.put(industry.getId(), industry.getIndustryName());
                }
            }
            for (Map<String, Object> row : industryRows) {
                Map<String, Object> item = new HashMap<>();
                item.put("industryId", mapLong(row, "industryId"));
                item.put("industryName", nameMap.get(mapLong(row, "industryId")));
                item.put("cnt", mapLong(row, "cnt"));
                industryDistribution.add(item);
            }
            Collections.sort(industryDistribution, (a, b) ->
                    Long.compare(mapLong(b, "cnt"), mapLong(a, "cnt")));
        }
        result.put("industryDistribution", industryDistribution);
        List<Map<String, Object>> statusRows = bizCustomerMapper.selectMaps(
                new QueryWrapper<BizCustomer>()
                        .select("status AS status", "COUNT(*) AS cnt")
                        .groupBy("status"));
        List<Map<String, Object>> statusDistribution = new ArrayList<>();
        if (statusRows != null) {
            for (Map<String, Object> row : statusRows) {
                Map<String, Object> item = new HashMap<>();
                item.put("status", mapLong(row, "status"));
                item.put("cnt", mapLong(row, "cnt"));
                statusDistribution.add(item);
            }
        }
        result.put("statusDistribution", statusDistribution);
        return result;
    }

    private Long mapLong(Map<String, Object> row, String key) {
        Object value = row.get(key);
        if (value == null) {
            value = row.get(key.toLowerCase());
        }
        if (value == null) {
            value = row.get(key.toUpperCase());
        }
        if (value == null) {
            return 0L;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return Long.valueOf(value.toString());
    }
}