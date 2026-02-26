package com.example.netdisk.mapper;

import com.example.netdisk.entity.DiskItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FavoriteMapper {

    int insert(@Param("userId") Long userId, @Param("diskItemId") Long diskItemId);

    int delete(@Param("userId") Long userId, @Param("diskItemId") Long diskItemId);

    int exists(@Param("userId") Long userId, @Param("diskItemId") Long diskItemId);

    List<DiskItem> pageFavorite(@Param("userId") Long userId, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    long countFavorite(@Param("userId") Long userId);

}
