package com.cxyzj.cxyzjback.Service;

import com.cxyzj.cxyzjback.Bean.user.User;

import java.util.List;

/**
 * @Package com.cxyzj.cxyzjback.Service
 * @Author Yaser
 * @Date 2018/07/29 15:25
 * @Description:
 */
public interface ITestService {
    String findAll();

    String findAllByID(String ID);

    String addUser(String email,String password,String gender);
}
