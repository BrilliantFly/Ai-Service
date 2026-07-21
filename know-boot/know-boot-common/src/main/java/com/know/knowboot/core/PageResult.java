package com.know.knowboot.core;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class PageResult<T> {

    /** 总记录数 **/
    private Long count;

    /** 当前页码 **/
    private Integer page_no;

    /** 每页条数 **/
    private Integer page_size;

    /** 扩展字段 **/
    private Map<String, Object> extend;

    /** 数据列表 **/
    private List<T> lists;

    /**
     * 创建简单的分页结果 (适用于已知分页信息的情况)
     *
     * @param list     数据列表
     * @param total    总记录数
     * @param pageNo   当前页码
     * @param pageSize 每页条数
     * @param <T>      泛型
     * @return PageResult
     */
    public static <T> PageResult<T> of(List<T> list, long total, int pageNo, int pageSize) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setCount(total);
        pageResult.setPage_no(pageNo);
        pageResult.setPage_size(pageSize);
        pageResult.setLists(list);
        return pageResult;
    }

    /**
     * MyBatisPlus分页
     *
     * @param iPage (分页)
     * @param <T>   (泛型)
     * @return PageList
     */
    public static <T> PageResult<T> iPageHandle(IPage<T> iPage) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setCount(iPage.getTotal());
        pageResult.setPage_no((int) iPage.getCurrent());
        pageResult.setPage_size((int) iPage.getSize());
        pageResult.setLists(iPage.getRecords());
        return pageResult;
    }

    /**
     * MyBatisPlus分页(数据额外处理)
     *
     * @param total   (总条数)
     * @param pageNo  (当前页码)
     * @param size    (每页条数)
     * @param list   (列表数据)
     * @param <T>    (泛型)
     * @return PageList
     */
    public static <T> PageResult<T> iPageHandle(Long total, Long pageNo, Long size, List<T> list) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setCount(total);
        pageResult.setPage_no(Math.toIntExact(pageNo));
        pageResult.setPage_size(Math.toIntExact(size));
        pageResult.setLists(list);
        return pageResult;
    }

    /**
     * MyBatisPlus分页(数据额外处理)
     *
     * @param total   (总条数)
     * @param pageNo  (当前页码)
     * @param size    (每页条数)
     * @param list    (列表数据)
     * @param extend  (扩展字段)
     * @param <T>     (泛型)
     * @return PageResult<T>
     */
    public static <T> PageResult<T> iPageHandle(Long total, Long pageNo, Long size, List<T> list, Map<String,Object> extend) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setCount(total);
        pageResult.setPage_no(Math.toIntExact(pageNo));
        pageResult.setPage_size(Math.toIntExact(size));
        pageResult.setLists(list);
        pageResult.setExtend(extend);
        return pageResult;
    }

    /**
     * 空分页结果
     *
     * @param <T> 泛型
     * @return 空分页结果
     */
    public static <T> PageResult<T> empty() {
        return of(new ArrayList<>(), 0L, 1, 10);
    }

}