package com.example.netdisk.security.utils;

import com.example.netdisk.exception.BusinessException;
import com.example.netdisk.vo.LoginUserVO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static LoginUserVO getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) { return null; }

        try {
            return (LoginUserVO) authentication.getPrincipal();
        } catch (Exception e) {
            throw new BusinessException(403, "用户未登录");
        }

    }

    public static Long getUserId() {
        LoginUserVO loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getId();
    }

}
