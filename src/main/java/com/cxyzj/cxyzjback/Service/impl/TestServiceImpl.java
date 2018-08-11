package com.cxyzj.cxyzjback.Service.impl;

import com.cxyzj.cxyzjback.Bean.user.User;
import com.cxyzj.cxyzjback.Repository.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.ITestService;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Package com.cxyzj.cxyzjback.Service.impl
 * @Author Yaser
 * @Date 2018/07/29 15:29
 * @Description:
 */
@Service
@Transactional
public class TestServiceImpl implements ITestService {
    @Autowired
    private UserJpaRepository userJpaRepository;
    private Response response;

    @Override
    public String findAll() {
        response = new Response();
        User user[] = userJpaRepository.findAll().toArray(new User[0]);
        if (user.length != 0) {
            response.insert(user);
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.NONE_USER,"还没有用户");
        }
    }

    @Override
    public String findByID(String ID) {
        response = new Response();
        User user = userJpaRepository.findByuserId(ID);
        if (user != null) {
            response.insert(user);
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.INVALID_USER,"用户不存在");
        }
    }

    @Override
    public String addUser(String email, String password, String gender) {
        response = new Response();
        User user = new User();
        user.setEmail(email);
        user.setGender(gender);
        user.setPassword(password);
        response.insert(userJpaRepository.save(user));
        return response.sendSuccess();
    }
}
