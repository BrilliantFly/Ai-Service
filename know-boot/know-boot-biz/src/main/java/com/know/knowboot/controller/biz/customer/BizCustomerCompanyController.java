package com.know.knowboot.controller.biz.customer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.biz.customer.BizCustomerCompany;
import com.know.knowboot.service.biz.customer.IBizCustomerCompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 客户企业控制器
 */
@Api(tags = "客户企业管理")
@RestController
@RequestMapping("/api/biz/customer/company")
public class BizCustomerCompanyController {

    @Autowired
    private IBizCustomerCompanyService bizCustomerCompanyService;

    @ApiOperation("分页查询客户企业")
    @GetMapping("/page")
    public AjaxResult<IPage<BizCustomerCompany>> page(
            BizCustomerCompany query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(bizCustomerCompanyService.page(query, pageNum, pageSize));
    }

    @ApiOperation("获取客户企业详情")
    @GetMapping("/{id}")
    public AjaxResult<BizCustomerCompany> getDetail(@PathVariable Long id) {
        return AjaxResult.success(bizCustomerCompanyService.getDetail(id));
    }

    @ApiOperation("创建客户企业")
    @PostMapping
    public AjaxResult<Long> create(@RequestBody BizCustomerCompany company) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizCustomerCompanyService.create(company, userId));
    }

    @ApiOperation("更新客户企业")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody BizCustomerCompany company) {
        Long userId = 1L; // TODO: 从Token获取用户ID
        return AjaxResult.success(bizCustomerCompanyService.update(company, userId));
    }

    @ApiOperation("删除客户企业")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(bizCustomerCompanyService.delete(id));
    }
}