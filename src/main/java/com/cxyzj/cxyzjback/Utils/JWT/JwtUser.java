package com.cxyzj.cxyzjback.Utils.JWT;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @Package com.cxyzj.cxyzjback.Utils
 * @Author Yaser
 * @Date 2018/08/09 11:08
 * @Description:
 */

public class JwtUser implements UserDetails {

    private String userId;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enable;

    public JwtUser(String userId, String password, Collection<? extends GrantedAuthority> authorities, boolean accountNonExpired, boolean credentialsNonExpired, boolean enable, boolean accountNonLocked) {
        this.userId = userId;
        this.password = password;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enable = enable;
        this.accountNonLocked = accountNonLocked;
    }

    public JwtUser(String userId, String password, Collection<? extends GrantedAuthority> authorities) {
        this(userId, password, authorities, true, true, true, true);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {//权限列表
        return authorities;
    }

    @Override
    public String getPassword() {//密码
        return password;
    }

    @Override
    public String getUsername() {//用户名
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {//账户未过期
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {//账户未锁
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {//管理证书为未过期
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {//是否激活
        return enable;
    }
}
