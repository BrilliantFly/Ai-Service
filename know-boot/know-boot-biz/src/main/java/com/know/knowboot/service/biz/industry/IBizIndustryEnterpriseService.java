package com.know.knowboot.service.biz.industry;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.biz.industry.BizIndustryEnterprise;

import java.util.List;

/**
 * 行业企业服务接口
 */
public interface IBizIndustryEnterpriseService {

    /**
     * 分页查询
     */
    IPage<BizIndustryEnterprise> page(Long industryId, String enterpriseType, String enterpriseName, Integer pageNum, Integer pageSize);

    /**
     * 获取详情
     */
    BizIndustryEnterprise getDetail(Long id);

    /**
     * 创建企业
     */
    Long create(BizIndustryEnterprise enterprise, Long userId);

    /**
     * 更新企业
     */
    Boolean update(BizIndustryEnterprise enterprise, Long userId);

    /**
     * 删除企业
     */
    Boolean delete(Long id);

    /**
     * 设置企业关联行业(全量替换)
     */
    Boolean setIndustries(Long id, List<Long> industryIds, Long userId);

    /**
     * 设置企业关联产品(全量替换)
     */
    Boolean setProducts(Long id, List<Long> productIds, Long userId);
}