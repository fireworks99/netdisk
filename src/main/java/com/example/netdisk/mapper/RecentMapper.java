package com.example.netdisk.mapper;

import com.example.netdisk.entity.DiskItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RecentMapper {

    int insertOrUpdate(@Param("userId") Long userId,
                       @Param("diskItemId") Long diskItemId,
                       @Param("action") String action);

    List<DiskItem> pageRecent(@Param("userId") Long userId,
                              @Param("offset") Integer offset,
                              @Param("pageSize") Integer pageSize);

    long countRecent(@Param("userId") Long userId);

}
