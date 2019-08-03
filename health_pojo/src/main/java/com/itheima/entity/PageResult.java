package com.itheima.entity;

import java.io.Serializable;
import java.util.List;

public class PageResult implements Serializable {
    /*
    * 分页结果封装
    *
    * */

    @Override
    public String toString() {
        return "PageResult{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }

    private Long total; //总纪录数
    private List rows; //当前页结果

    public PageResult() {
    }

    public PageResult(Long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
