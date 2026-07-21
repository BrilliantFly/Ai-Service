package com.know.knowboot.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EsPage<T> implements Serializable {

    /**
     * 分页页码, 查询哪一页的数据
     */
    private int pageNo;
    /**
     * 每一页显示记录数量
     */
    private int pageSize;
    /**
     * 查询总记录数量
     */
    private int total;
    /**
     * 总页数
     */
    private int pages;
    /**
     * 查询数据结果集
     */
    private List<T> result;

    /**
     * 总页数计算
     *
     * @param total
     */
    public void setTotal(int total) {
        this.total = total;
        if (total > 0L) {
            if (this.total % this.pageSize == 0) {
                this.pages = this.total / this.pageSize;
            } else {
                this.pages = this.total / this.pageSize + 1;
            }
        }
    }
}
