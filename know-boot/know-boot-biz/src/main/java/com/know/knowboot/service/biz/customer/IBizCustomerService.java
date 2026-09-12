package com.know.knowboot.service.biz.customer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.biz.customer.BizCustomer;
import com.know.knowboot.entity.biz.customer.BizCustomerIndustry;

import java.util.List;
import java.util.Map;

/**
 * 客户服务接口
 */
public interface IBizCustomerService {

    /**
     * 分页查询
     */
    IPage<BizCustomer> page(BizCustomer query, Integer pageNum, Integer pageSize);

    /**
     * 获取详情
     */
    BizCustomer getDetail(Long id);

    /**
     * 创建客户
     */
    Long create(BizCustomer customer, Long userId);

    /**
     * 更新客户
     */
    Boolean update(BizCustomer customer, Long userId);

    /**
     * 删除客户
     */
    Boolean delete(Long id);

    /**
     * 更新客户状态
     */
    Boolean updateStatus(Long id, Integer status, Long userId);

    /**
     * 设置客户行业
     */
    Boolean setIndustries(Long id, List<BizCustomerIndustry> relations, Long userId);

    /**
     * 统计
     */
    Map<String, Object> statistics();
}