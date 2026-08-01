package com.know.knowboot.service.plan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.know.knowboot.entity.plan.HomeSlogan;

import java.util.List;

/**
 * 首页标语服务接口
 */
public interface IHomeSloganService {

    /**
     * 分页查询标语
     */
    IPage<HomeSlogan> page(HomeSlogan query, Integer pageNum, Integer pageSize);

    /**
     * 查询列表
     */
    List<HomeSlogan> list(HomeSlogan query);

    /**
     * 新增标语
     */
    boolean add(HomeSlogan slogan, Long userId);

    /**
     * 修改标语
     */
    boolean update(HomeSlogan slogan);

    /**
     * 删除标语
     */
    boolean delete(Long id);

    /**
     * 获取当前生效标语（按时间窗 + 状态 + 排序取第一条）
     */
    HomeSlogan getCurrent();
}
