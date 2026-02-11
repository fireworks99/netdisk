package com.example.netdisk.dto.DiskItem;

import lombok.Data;

@Data
public class DiskItemQuery {

    private Long ownerId;

    private Long parentId;

    private String keyword;

    private Boolean deleted;

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    private Integer offset;

}
