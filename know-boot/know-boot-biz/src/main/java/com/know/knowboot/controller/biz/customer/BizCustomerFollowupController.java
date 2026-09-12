package com.know.knowboot.controller.biz.customer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.biz.customer.BizCustomerFollowup;
import com.know.knowboot.service.biz.customer.IBizCustomerFollowupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 客户跟进控制器
 */
@Api(tags = "客户跟进管理")
@RestController
@RequestMapping("/api/biz/customer/followup")
public class BizCustomerFollowupController {

    @Autowired
    private IBizCustomerFollowupService bizCustomerFollowupService;

    @ApiOperation("分页查询客户跟进记录")
    @GetMapping("/page")
    public AjaxResult<IPage<BizCustomerFollowup>> page(
            BizCustomerFollowup query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(bizCustomerFollowupService.page(query, pageNum, pageSize));
    }

    @ApiOperation("获取跟进记录详情")
    @GetMapping("/{id}")
    public AjaxResult<BizCustomerFollowup> getDetail(@PathVariable Long id) {
        return AjaxResult.success(bizCustomerFollowupService.getDetail(id));
    }

    @ApiOperation("创建跟进记录")
    @PostMapping
    public AjaxResult<Long> create(@RequestBody BizCustomerFollowup followup) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizCustomerFollowupService.create(followup, userId));
    }

    @ApiOperation("更新跟进记录")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody BizCustomerFollowup followup) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizCustomerFollowupService.update(followup, userId));
    }

    @ApiOperation("删除跟进记录")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(bizCustomerFollowupService.delete(id));
    }
}