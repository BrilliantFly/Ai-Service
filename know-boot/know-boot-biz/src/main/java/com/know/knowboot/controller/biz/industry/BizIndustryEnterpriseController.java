package com.know.knowboot.controller.biz.industry;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.biz.industry.BizIndustryEnterprise;
import com.know.knowboot.service.biz.industry.IBizIndustryEnterpriseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 行业企业控制器
 */
@Api(tags = "行业企业管理")
@RestController
@RequestMapping("/api/biz/industry/enterprise")
public class BizIndustryEnterpriseController {

    @Autowired
    private IBizIndustryEnterpriseService bizIndustryEnterpriseService;

    @ApiOperation("分页查询行业企业")
    @GetMapping("/list")
    public AjaxResult<IPage<BizIndustryEnterprise>> list(
            @RequestParam(required = false) Long industryId,
            @RequestParam(required = false) String enterpriseType,
            @RequestParam(required = false) String enterpriseName,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(bizIndustryEnterpriseService.page(industryId, enterpriseType, enterpriseName, pageNum, pageSize));
    }

    @ApiOperation("获取行业企业详情")
    @GetMapping("/{id}")
    public AjaxResult<BizIndustryEnterprise> getDetail(@PathVariable Long id) {
        return AjaxResult.success(bizIndustryEnterpriseService.getDetail(id));
    }

    @ApiOperation("创建行业企业")
    @PostMapping
    public AjaxResult<Long> create(@RequestBody BizIndustryEnterprise enterprise) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizIndustryEnterpriseService.create(enterprise, userId));
    }

    @ApiOperation("更新行业企业")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody BizIndustryEnterprise enterprise) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizIndustryEnterpriseService.update(enterprise, userId));
    }

    @ApiOperation("删除行业企业")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(bizIndustryEnterpriseService.delete(id));
    }

    @ApiOperation("设置企业关联行业")
    @PostMapping("/{id}/industries")
    public AjaxResult<Boolean> setIndustries(@PathVariable Long id, @RequestBody List<Long> industryIds) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizIndustryEnterpriseService.setIndustries(id, industryIds, userId));
    }

    @ApiOperation("设置企业关联产品")
    @PostMapping("/{id}/products")
    public AjaxResult<Boolean> setProducts(@PathVariable Long id, @RequestBody List<Long> productIds) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizIndustryEnterpriseService.setProducts(id, productIds, userId));
    }
}