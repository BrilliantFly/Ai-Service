package com.know.knowboot.controller.biz.industry;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.biz.industry.BizIndustryProduct;
import com.know.knowboot.service.biz.industry.IBizIndustryProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 行业产品控制器
 */
@Api(tags = "行业产品管理")
@RestController
@RequestMapping("/api/biz/industry/product")
public class BizIndustryProductController {

    @Autowired
    private IBizIndustryProductService bizIndustryProductService;

    @ApiOperation("分页查询行业产品")
    @GetMapping("/list")
    public AjaxResult<IPage<BizIndustryProduct>> list(
            @RequestParam(required = false) Long industryId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String productName,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(bizIndustryProductService.page(industryId, category, productName, pageNum, pageSize));
    }

    @ApiOperation("获取行业产品详情")
    @GetMapping("/{id}")
    public AjaxResult<BizIndustryProduct> getDetail(@PathVariable Long id) {
        return AjaxResult.success(bizIndustryProductService.getDetail(id));
    }

    @ApiOperation("创建行业产品")
    @PostMapping
    public AjaxResult<Long> create(@RequestBody BizIndustryProduct product) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizIndustryProductService.create(product, userId));
    }

    @ApiOperation("更新行业产品")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody BizIndustryProduct product) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizIndustryProductService.update(product, userId));
    }

    @ApiOperation("删除行业产品")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(bizIndustryProductService.delete(id));
    }

    @ApiOperation("设置产品关联行业")
    @PostMapping("/{id}/industries")
    public AjaxResult<Boolean> setIndustries(@PathVariable Long id, @RequestBody List<Long> industryIds) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizIndustryProductService.setIndustries(id, industryIds, userId));
    }
}