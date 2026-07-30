package com.example.netdisk.security.service;

import com.example.netdisk.entity.SysUser;
import com.example.netdisk.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser user = sysUserMapper.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        List<String> roles = sysUserMapper.findRolesByUserId(user.getId());
        List<String> perms = sysUserMapper.findPermsByUserId(user.getId());

        List<GrantedAuthority> authorities = new ArrayList<>();

        roles.forEach(r -> authorities.add(new SimpleGrantedAuthority(r)));
        perms.forEach(p -> authorities.add(new SimpleGrantedAuthority(p)));

        return new User(user.getUsername(), user.getPassword(), user.getStatus(), true, true, true, authorities);
    }
}
