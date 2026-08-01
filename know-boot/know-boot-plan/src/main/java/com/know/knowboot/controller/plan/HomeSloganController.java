package com.know.knowboot.controller.plan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.core.AjaxResult;
import com.know.knowboot.entity.plan.HomeSlogan;
import com.know.knowboot.service.plan.IHomeSloganService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页标语控制器
 */
@Api(tags = "首页标语")
@RestController
@RequestMapping("/api/plan/slogan")
public class HomeSloganController {

    @Autowired
    private IHomeSloganService homeSloganService;

    @ApiOperation("分页查询标语")
    @GetMapping("/page")
    public AjaxResult<IPage<HomeSlogan>> page(
            HomeSlogan query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(homeSloganService.page(query, pageNum, pageSize));
    }

    @ApiOperation("查询标语列表")
    @GetMapping("/list")
    public AjaxResult<List<HomeSlogan>> list(HomeSlogan query) {
        return AjaxResult.success(homeSloganService.list(query));
    }

    @ApiOperation("获取当前生效标语")
    @GetMapping("/current")
    public AjaxResult<Map<String, Object>> current() {
        HomeSlogan slogan = homeSloganService.getCurrent();
        Map<String, Object> result = new LinkedHashMap<>();
        if (slogan != null) {
            result.put("content", slogan.getContent());
            result.put("emoji", slogan.getEmoji());
        } else {
            result.put("content", "努力是光，坚持是路");
            result.put("emoji", "✨");
        }
        return AjaxResult.success(result);
    }

    @ApiOperation("新增标语")
    @PostMapping
    public AjaxResult<Boolean> add(@RequestBody HomeSlogan slogan) {
        // TODO: 从Token获取用户ID
        Long userId = 1L;
        return AjaxResult.success(homeSloganService.add(slogan, userId));
    }

    @ApiOperation("修改标语")
    @PutMapping
    public AjaxResult<Boolean> update(@RequestBody HomeSlogan slogan) {
        return AjaxResult.success(homeSloganService.update(slogan));
    }

    @ApiOperation("删除标语")
    @DeleteMapping("/{id}")
    public AjaxResult<Boolean> delete(@PathVariable Long id) {
        return AjaxResult.success(homeSloganService.delete(id));
    }
}
