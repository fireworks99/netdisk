package com.example.netdisk.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysUser {

    private Long id;
    private String username;
    private String password;
    private String nickname;
    private Boolean status;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
