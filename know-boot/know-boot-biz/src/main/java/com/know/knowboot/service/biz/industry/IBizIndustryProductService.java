package com.know.knowboot.service.biz.industry;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.biz.industry.BizIndustryProduct;

import java.util.List;

/**
 * 行业产品服务接口
 */
public interface IBizIndustryProductService {

    /**
     * 分页查询
     */
    IPage<BizIndustryProduct> page(Long industryId, String category, String productName, Integer pageNum, Integer pageSize);

    /**
     * 获取详情
     */
    BizIndustryProduct getDetail(Long id);

    /**
     * 创建产品
     */
    Long create(BizIndustryProduct product, Long userId);

    /**
     * 更新产品
     */
    Boolean update(BizIndustryProduct product, Long userId);

    /**
     * 删除产品
     */
    Boolean delete(Long id);

    /**
     * 设置产品关联行业(全量替换)
     */
    Boolean setIndustries(Long id, List<Long> industryIds, Long userId);
}