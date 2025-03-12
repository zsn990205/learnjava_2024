package com.bite.book.model;

import lombok.Data;

import java.util.List;

@Data
public class pageResult<T> {
    //book的当前页的记录
    private List<T> records;
    //返回的总的条数是啥
    private Integer total;

    private PageRequest pageRequest;
    public pageResult(List<T> records, Integer total,PageRequest pageRequest) {
        this.records = records;
        this.total = total;
        this.pageRequest = pageRequest;
    }
}
