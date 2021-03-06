package com.cxyzj.cxyzjback.Utils.JWT;

import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Package com.cxyzj.cxyzjback.Service.impl
 * @Author Yaser
 * @Date 2018/08/09 15:15
 * @Description:
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserJpaRepository userJpaRepository;

    /**
     * @param username 用户id
     * @return 用户信息
     * @throws UsernameNotFoundException 用户未找到
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userJpaRepository.findByUserId(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No User found with userId '%s'", username));
        } else {
            return JwtUserFactory.create(user);
        }
    }
}
