package com.know.knowboot.controller.biz.industry;

import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.biz.industry.BizIndustryMarket;
import com.know.knowboot.service.biz.industry.IBizIndustryMarketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 行业市场控制器
 */
@Api(tags = "行业市场管理")
@RestController
@RequestMapping("/api/biz/industry/market")
public class BizIndustryMarketController {

    @Autowired
    private IBizIndustryMarketService bizIndustryMarketService;

    @ApiOperation("按行业获取市场信息")
    @GetMapping("/{industryId}")
    public AjaxResult<BizIndustryMarket> getByIndustryId(@PathVariable Long industryId) {
        return AjaxResult.success(bizIndustryMarketService.getByIndustryId(industryId));
    }

    @ApiOperation("按行业获取市场列表")
    @GetMapping("/list")
    public AjaxResult<List<BizIndustryMarket>> list(@RequestParam(required = false) Long industryId) {
        return AjaxResult.success(bizIndustryMarketService.getListByIndustryId(industryId));
    }

    @ApiOperation("创建行业市场")
    @PostMapping
    public AjaxResult<Long> create(@RequestBody BizIndustryMarket market) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizIndustryMarketService.createMarket(market, userId));
    }

    @ApiOperation("保存行业市场信息")
    @PutMapping("/{industryId}")
    public AjaxResult<Boolean> save(@PathVariable Long industryId, @RequestBody BizIndustryMarket market) {
        if (market.getIndustryIds() == null || market.getIndustryIds().isEmpty()) {
            market.setIndustryIds(Collections.singletonList(industryId));
        }
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizIndustryMarketService.save(market, userId));
    }
}