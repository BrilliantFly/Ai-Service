package com.know.knowboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.dto.SysDeptJobAssignDTO;
import com.know.knowboot.entity.tenant.SysDept;
import com.know.knowboot.entity.tenant.SysUser;
import com.know.knowboot.service.ISysDeptService;
import com.know.knowboot.service.tenant.ISysDeptJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 */
@Api(tags = "部门管理")
@RestController
@RequestMapping("/api/system/dept")
public class SysDeptController {

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private ISysDeptJobService sysDeptJobService;

    @ApiOperation("分页查询部门")
    @GetMapping("/page")
    public AjaxResult<IPage<SysDept>> page(
            SysDept query,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(sysDeptService.page(query, pageNum, pageSize));
    }

    @ApiOperation("获取所有部门")
    @GetMapping("/list")
    public AjaxResult<List<SysDept>> list() {
        return AjaxResult.success(sysDeptService.listAll());
    }

    @ApiOperation("获取部门树")
    @GetMapping("/tree")
    public AjaxResult<List<SysDept>> tree() {
        return AjaxResult.success(sysDeptService.buildTree());
    }

    @ApiOperation("获取子部门")
    @GetMapping("/children/{parentId}")
    public AjaxResult<List<SysDept>> children(@ApiParam("父���门ID") @PathVariable Long parentId) {
        return AjaxResult.success(sysDeptService.listByParentId(parentId));
    }

    @ApiOperation("获取部门详情")
    @GetMapping("/{id}")
    public AjaxResult<SysDept> get(@ApiParam("部门ID") @PathVariable Long id) {
        return AjaxResult.success(sysDeptService.getById(id));
    }

    @ApiOperation("新增部门")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody SysDept dept) {
        return AjaxResult.success(sysDeptService.add(dept));
    }

    @ApiOperation("修改部门")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody SysDept dept) {
        return AjaxResult.success(sysDeptService.update(dept));
    }

    @ApiOperation("删除部门")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@ApiParam("部门ID") @PathVariable Long id) {
        return AjaxResult.success(sysDeptService.delete(id));
    }

    // ==================== 部门-用户管理 ====================

    /**
     * 获取部门下的用户列表
     */
    @ApiOperation("获取部门下的用户列表")
    @GetMapping("/{deptId}/users")
    public AjaxResult<List<SysUser>> getDeptUsers(@ApiParam("部门ID") @PathVariable Long deptId) {
        return AjaxResult.success(sysDeptService.getDeptUsers(deptId));
    }

    // ==================== 部门-岗位管理 ====================

    /**
     * 获取部门关联的岗位列表
     */
    @ApiOperation("获取部门关联的岗位列表")
    @GetMapping("/{deptId}/jobs")
    public AjaxResult<List<Long>> getDeptJobs(@ApiParam("部门ID") @PathVariable Long deptId) {
        List<Long> jobIds = sysDeptJobService.selectJobIdsByDeptId(deptId);
        return AjaxResult.success(jobIds);
    }

    /**
     * 分配岗位给部门
     */
    @ApiOperation("分配岗位给部门")
    @PostMapping("/assignJobs")
    public AjaxResult<Boolean> assignJobs(@RequestBody SysDeptJobAssignDTO dto) {
        sysDeptJobService.assignJobs(dto.getDeptId(), dto.getJobIds());
        return AjaxResult.success(true);
    }
}