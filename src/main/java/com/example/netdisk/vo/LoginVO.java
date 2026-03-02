package com.example.netdisk.vo;

import com.example.netdisk.entity.SysUser;
import lombok.Data;

import java.util.Date;

@Data
public class LoginVO {

    private Long userId;
    private String username;
    private String nickname;
    private Boolean status;
    private String token;
    private Date expiration;

}
