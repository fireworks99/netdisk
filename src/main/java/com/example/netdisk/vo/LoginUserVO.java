package com.example.netdisk.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginUserVO {

    private Long id;
    private String username;
    private List<String> roles;
    private List<String> perms;

}
