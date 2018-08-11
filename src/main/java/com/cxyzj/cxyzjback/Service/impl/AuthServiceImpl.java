package com.cxyzj.cxyzjback.Service.impl;

import com.cxyzj.cxyzjback.Bean.user.Role;
import com.cxyzj.cxyzjback.Bean.user.User;
import com.cxyzj.cxyzjback.Repository.UserJpaRepository;
import com.cxyzj.cxyzjback.Repository.UserRoleJpaRepository;
import com.cxyzj.cxyzjback.Service.AuthService;
import com.cxyzj.cxyzjback.Utils.JWT.JWTUtils;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.IllegalFormatException;
import java.util.List;

/**
 * @Package com.cxyzj.cxyzjback.Service.impl
 * @Author Yaser
 * @Date 2018/08/10 15:38
 * @Description:
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserJpaRepository userJpaRepository;
    private Response response;

    @Override
    public User register(User user) {
        return null;
    }

    @Override
    public String login(String email, String phone, String password) throws NoSuchFieldException {
        JWTUtils jwtUtils = new JWTUtils();
        User user;
        if (email != null) {
            user = userJpaRepository.findByEmailAndPassword(email, password);
        } else {
            if (phone == null) {
                throw new NoSuchFieldException("phone字段与email字段不可同时为空！");
            } else {
                user = userJpaRepository.findByPhoneAndPassword(phone, password);
            }
        }
        response = new Response();
        if (user != null) {
            String token = jwtUtils.generateToken(user);//生成用户token
            log.info("token:  " + token);
            response.insert("token", token);
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.NONE_USER, "用户不存在！");
        }

    }

    @Override
    public String refresh(String oldToken) {
        return null;
    }
}
