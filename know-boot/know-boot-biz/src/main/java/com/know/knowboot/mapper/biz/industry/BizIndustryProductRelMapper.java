package com.know.knowboot.mapper.biz.industry;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.biz.industry.BizIndustryProductRel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 行业-产品关联Mapper
 */
@Mapper
public interface BizIndustryProductRelMapper extends IBaseMapper<BizIndustryProductRel> {

    /**
     * 物理删除某产品的全部关联(含逻辑删除残留),供全量替换使用
     */
    @Delete("DELETE FROM biz_industry_product_rel WHERE product_id = #{productId}")
    int hardDeleteByProductId(@Param("productId") Long productId);
}