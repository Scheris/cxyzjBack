package com.cxyzj.cxyzjback.Service;

import com.cxyzj.cxyzjback.Bean.User;

import java.util.List;

/**
 * @Package com.cxyzj.cxyzjback.Service
 * @Author Yaser
 * @Date 2018/07/29 15:25
 * @Description:
 */
public interface IUserService {
    List<User> findAll();

    User findAllByID(String ID);
}
