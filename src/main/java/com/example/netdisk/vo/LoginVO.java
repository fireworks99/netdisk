package com.example.netdisk.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LoginVO {

    private Long userId;
    private String username;
    private String nickname;
    private Boolean status;
    private String token;
    private Date expiration;
    private List<String> roles;
    private List<String> perms;

}
