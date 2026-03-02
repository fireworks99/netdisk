package com.example.netdisk.mapper;

import com.example.netdisk.dto.DiskItem.DiskItemQuery;
import com.example.netdisk.entity.DiskItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiskItemMapper {

    int insert(DiskItem item);

    DiskItem findById(Long id);

    List<DiskItem> findByParent(
        @Param("ownerId") Long ownerId,
        @Param("parentId") Long parentId
    );

    int updateParent(
        @Param("id") Long id,
        @Param("parentId") Long parentId
    );

    int softDelete(@Param("id") Long id);

    List<DiskItem> findDeleted(@Param("ownerId") Long ownerId);

    int restore(Long id);

    int deletePhysical(Long id);

    List<DiskItem> findChildren(Long parentId);

    List<Long> findExpiredDeleted(@Param("days") int days);

    List<DiskItem> pageQuery(DiskItemQuery query);

    Long countQuery(DiskItemQuery query);

    int batchSoftDelete(@Param("ownerId") Long ownerId, @Param("ids") List<Long> ids);

    int batchMove(@Param("ownerId") Long ownerId, @Param("ids") List<Long> ids, @Param("targetParentId") Long targetParentId);

}
