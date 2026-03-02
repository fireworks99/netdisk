package com.example.netdisk.dto.Role;

import com.example.netdisk.entity.SysRole;
import lombok.Data;

import java.util.List;

@Data
public class RoleCreateDTO {

    private SysRole role;
    private List<Long> permIds;

}
