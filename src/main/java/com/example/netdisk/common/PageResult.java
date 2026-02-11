package com.example.netdisk.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor//自动生成一个全参构造方法
public class PageResult<T>{
    private Long total;

    private List<T> records;
}
