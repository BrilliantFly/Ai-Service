package com.know.knowboot.mapper.biz.industry;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.biz.industry.BizIndustryEnterpriseRel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 行业-企业关联Mapper
 */
@Mapper
public interface BizIndustryEnterpriseRelMapper extends IBaseMapper<BizIndustryEnterpriseRel> {

    /**
     * 物理删除某企业的全部行业关联(含逻辑删除残留),供全量替换使用
     */
    @Delete("DELETE FROM biz_industry_enterprise_rel WHERE enterprise_id = #{enterpriseId}")
    int hardDeleteByEnterpriseId(@Param("enterpriseId") Long enterpriseId);
}