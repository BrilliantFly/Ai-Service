package com.know.knowboot.service.biz.customer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.biz.customer.BizCustomerFollowup;

/**
 * 客户跟进服务接口
 */
public interface IBizCustomerFollowupService {

    /**
     * 分页查询
     */
    IPage<BizCustomerFollowup> page(BizCustomerFollowup query, Integer pageNum, Integer pageSize);

    /**
     * 获取详情
     */
    BizCustomerFollowup getDetail(Long id);

    /**
     * 创建跟进记录
     */
    Long create(BizCustomerFollowup followup, Long userId);

    /**
     * 更新跟进记录
     */
    Boolean update(BizCustomerFollowup followup, Long userId);

    /**
     * 删除跟进记录
     */
    Boolean delete(Long id);
}