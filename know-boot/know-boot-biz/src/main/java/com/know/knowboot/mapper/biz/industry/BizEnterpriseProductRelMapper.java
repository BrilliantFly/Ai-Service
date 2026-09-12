package com.know.knowboot.mapper.biz.industry;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.biz.industry.BizEnterpriseProductRel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 企业-产品关联Mapper
 */
@Mapper
public interface BizEnterpriseProductRelMapper extends IBaseMapper<BizEnterpriseProductRel> {

    /**
     * 物理删除某企业的全部产品关联(含逻辑删除残留),供全量替换使用
     */
    @Delete("DELETE FROM biz_enterprise_product_rel WHERE enterprise_id = #{enterpriseId}")
    int hardDeleteByEnterpriseId(@Param("enterpriseId") Long enterpriseId);
}