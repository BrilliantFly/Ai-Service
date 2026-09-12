package com.know.knowboot.service.biz.industry;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.biz.industry.BizIndustry;

import java.util.List;
import java.util.Map;

/**
 * 行业主体服务接口
 */
public interface IBizIndustryService {

    /**
     * 分页查询
     */
    IPage<BizIndustry> page(BizIndustry query, Integer pageNum, Integer pageSize);

    /**
     * 全部行业列表（下拉框）
     */
    List<BizIndustry> listAll();

    /**
     * 获取详情
     */
    BizIndustry getDetail(Long id);

    /**
     * 创建行业
     */
    Long create(BizIndustry industry, Long userId);

    /**
     * 更新行业
     */
    Boolean update(BizIndustry industry, Long userId);

    /**
     * 删除行业
     */
    Boolean delete(Long id);

    /**
     * 分页查询关联客户
     */
    IPage<Map<String, Object>> getCustomers(Long industryId, String keyword, Integer pageNum, Integer pageSize);

    /**
     * 统计
     */
    Map<String, Object> statistics();
}