package com.cxyzj.cxyzjback.Service;

/**
 * @Package com.cxyzj.cxyzjback.Service
 * @Author Yaser
 * @Date 2018/07/29 15:25
 * @Description:
 */
public interface ITestService {
    String findAll();

    String findByID(String ID);

    String addUser(String email,String password,String gender);
}
