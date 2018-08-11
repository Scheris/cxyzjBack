package com.cxyzj.cxyzjback.Service.impl;

import com.cxyzj.cxyzjback.Bean.user.Role;
import com.cxyzj.cxyzjback.Bean.user.User;
import com.cxyzj.cxyzjback.Data.User.LoginResult;
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
            user = userJpaRepository.findByEmailAndPassword(email, password);//读取信息
        } else {
            if (phone == null) {
                throw new NoSuchFieldException("phone字段与email字段不可同时为空！");
            } else {
                user = userJpaRepository.findByPhoneAndPassword(phone, password);//读取信息
            }
        }
        response = new Response();
        if (user != null) {
            String token = jwtUtils.generateToken(user);//生成用户token
            log.info("token:  " + token);
            //注意：后端从数据库中查找到的数据不可以直接返回，需要重新自定义一个数据结构！
            LoginResult loginResult = new LoginResult(user);//转换返回的数据为前端需要的数据
            response.insert("token", token);
            response.insert("user", loginResult);
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
