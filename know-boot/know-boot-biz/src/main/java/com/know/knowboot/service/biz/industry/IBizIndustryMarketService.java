package com.know.knowboot.service.biz.industry;

import com.know.knowboot.entity.biz.industry.BizIndustryMarket;

import java.util.List;

/**
 * 行业市场服务接口
 */
public interface IBizIndustryMarketService {

    /**
     * 按行业ID获取市场信息(取关联的市场列表第一个)
     */
    BizIndustryMarket getByIndustryId(Long industryId);

    /**
     * 按行业ID获取关联的市场列表
     */
    List<BizIndustryMarket> getListByIndustryId(Long industryId);

    /**
     * 保存市场信息（有id则按市场id更新，否则创建）
     */
    Boolean save(BizIndustryMarket market, Long userId);

    /**
     * 创建市场并维护行业关联
     */
    Long createMarket(BizIndustryMarket market, Long userId);
}