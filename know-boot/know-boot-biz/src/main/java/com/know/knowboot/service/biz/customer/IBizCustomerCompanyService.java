package com.know.knowboot.service.biz.customer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.biz.customer.BizCustomerCompany;

/**
 * 客户企业服务接口
 */
public interface IBizCustomerCompanyService {

    /**
     * 分页查询
     */
    IPage<BizCustomerCompany> page(BizCustomerCompany query, Integer pageNum, Integer pageSize);

    /**
     * 获取详情
     */
    BizCustomerCompany getDetail(Long id);

    /**
     * 创建企业
     */
    Long create(BizCustomerCompany company, Long userId);

    /**
     * 更新企业
     */
    Boolean update(BizCustomerCompany company, Long userId);

    /**
     * 删除企业
     */
    Boolean delete(Long id);
}