package com.cxyzj.cxyzjback.Utils.JWT;

import com.cxyzj.cxyzjback.Bean.user.User;
import com.cxyzj.cxyzjback.Catch.RoleList;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Package com.cxyzj.cxyzjback.Utils
 * @Author Yaser
 * @Date 2018/08/09 15:11
 * @Description: JwtUser工厂类
 */
public class JwtUserFactory {
    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(user.getUserId(), user.getPassword(), mapToGrantedAuthority(RoleList.getRoles().getRole(user.getRoleId())));
    }

    private static List<GrantedAuthority> mapToGrantedAuthority(String role) {

        return Stream.of(role).map(SimpleGrantedAuthority::new).collect(Collectors.toList());//将string转为list
    }
}
