package com.cxyzj.cxyzjback.Service.Interface.Other;

import org.springframework.stereotype.Service;

/**
 * @Package com.cxyzj.cxyzjback.Service
 * @Author Yaser
 * @Date 2018/07/29 15:25
 * @Description:
 */
public interface ITestService {
    String findAll();

    String findByID(String ID);

    String addUser(String email, String password, int gender, String nickname, String phone, String headUrl);

}
