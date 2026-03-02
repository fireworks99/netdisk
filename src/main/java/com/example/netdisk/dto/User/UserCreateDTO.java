package com.example.netdisk.dto.User;

import com.example.netdisk.entity.SysUser;
import lombok.Data;

import java.util.List;

@Data
public class UserCreateDTO {

    private SysUser user;
    private List<Long> roleIds;
}
