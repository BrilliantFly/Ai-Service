package com.know.knowboot.controller.biz.customer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.biz.customer.BizCustomer;
import com.know.knowboot.entity.biz.customer.BizCustomerIndustry;
import com.know.knowboot.service.biz.customer.IBizCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 客户控制器
 */
@Api(tags = "客户管理")
@RestController
@RequestMapping("/api/biz/customer")
public class BizCustomerController {

    @Autowired
    private IBizCustomerService bizCustomerService;

    @ApiOperation("分页查询客户")
    @GetMapping("/page")
    public AjaxResult<IPage<BizCustomer>> page(
            BizCustomer query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(bizCustomerService.page(query, pageNum, pageSize));
    }

    @ApiOperation("获取客户详情")
    @GetMapping("/{id}")
    public AjaxResult<BizCustomer> getDetail(@PathVariable Long id) {
        return AjaxResult.success(bizCustomerService.getDetail(id));
    }

    @ApiOperation("创建客户")
    @PostMapping
    public AjaxResult<Long> create(@RequestBody BizCustomer customer) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizCustomerService.create(customer, userId));
    }

    @ApiOperation("更新客户")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody BizCustomer customer) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizCustomerService.update(customer, userId));
    }

    @ApiOperation("删除客户")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(bizCustomerService.delete(id));
    }

    @ApiOperation("更新客户状态")
    @PutMapping("/{id}/status")
    public AjaxResult<Boolean> updateStatus(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        Object statusValue = body.get("status");
        Integer status = statusValue == null ? null : Integer.valueOf(String.valueOf(statusValue));
        return AjaxResult.success(bizCustomerService.updateStatus(id, status, userId));
    }

    @ApiOperation("设置客户行业")
    @PostMapping("/{id}/industries")
    public AjaxResult<Boolean> setIndustries(@PathVariable Long id, @RequestBody List<BizCustomerIndustry> relations) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizCustomerService.setIndustries(id, relations, userId));
    }

    @ApiOperation("客户统计")
    @GetMapping("/statistics")
    public AjaxResult<Map<String, Object>> statistics() {
        return AjaxResult.success(bizCustomerService.statistics());
    }
}