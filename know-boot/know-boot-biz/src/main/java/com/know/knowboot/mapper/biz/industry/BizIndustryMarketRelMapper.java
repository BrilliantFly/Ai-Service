package com.know.knowboot.mapper.biz.industry;

import com.know.knowboot.core.basics.IBaseMapper;
import com.know.knowboot.entity.biz.industry.BizIndustryMarketRel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 行业-市场关联Mapper
 */
@Mapper
public interface BizIndustryMarketRelMapper extends IBaseMapper<BizIndustryMarketRel> {

    /**
     * 物理删除某市场的全部行业关联(含逻辑删除残留),供全量替换使用
     */
    @Delete("DELETE FROM biz_industry_market_rel WHERE market_id = #{marketId}")
    int hardDeleteByMarketId(@Param("marketId") Long marketId);
}