package com.example.netdisk.dto.DiskItem;

import lombok.Data;

import java.util.List;

@Data
public class BatchOperateRequest {

    // 当前文件（夹）ID
    private List<Long> itemIds;

    // 目标目录（批量移动）
    private Long targetParentId;
}
