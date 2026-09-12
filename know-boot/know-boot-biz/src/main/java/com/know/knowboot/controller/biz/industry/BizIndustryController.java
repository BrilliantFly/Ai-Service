package com.know.knowboot.controller.biz.industry;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.biz.industry.BizIndustry;
import com.know.knowboot.service.biz.industry.IBizIndustryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 行业主体控制器
 */
@Api(tags = "行业管理")
@RestController
@RequestMapping("/api/biz/industry")
public class BizIndustryController {

    @Autowired
    private IBizIndustryService bizIndustryService;

    @ApiOperation("分页查询行业")
    @GetMapping("/page")
    public AjaxResult<IPage<BizIndustry>> page(
            BizIndustry query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(bizIndustryService.page(query, pageNum, pageSize));
    }

    @ApiOperation("行业列表(下拉框)")
    @GetMapping("/list")
    public AjaxResult<List<BizIndustry>> list() {
        return AjaxResult.success(bizIndustryService.listAll());
    }

    @ApiOperation("获取行业详情")
    @GetMapping("/{id}")
    public AjaxResult<BizIndustry> getDetail(@PathVariable Long id) {
        return AjaxResult.success(bizIndustryService.getDetail(id));
    }

    @ApiOperation("创建行业")
    @PostMapping
    public AjaxResult<Long> create(@RequestBody BizIndustry industry) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizIndustryService.create(industry, userId));
    }

    @ApiOperation("更新行业")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody BizIndustry industry) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizIndustryService.update(industry, userId));
    }

    @ApiOperation("删除行业")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(bizIndustryService.delete(id));
    }

    @ApiOperation("分页查询关联客户")
    @GetMapping("/{id}/customers")
    public AjaxResult<IPage<Map<String, Object>>> customers(
            @PathVariable Long id,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(bizIndustryService.getCustomers(id, keyword, pageNum, pageSize));
    }

    @ApiOperation("行业统计")
    @GetMapping("/statistics")
    public AjaxResult<Map<String, Object>> statistics() {
        return AjaxResult.success(bizIndustryService.statistics());
    }
}