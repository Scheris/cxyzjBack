package com.cxyzj.cxyzjback.Service.impl;

import com.cxyzj.cxyzjback.Bean.User;
import com.cxyzj.cxyzjback.Repository.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Package com.cxyzj.cxyzjback.Service.impl
 * @Author Yaser
 * @Date 2018/07/29 15:29
 * @Description:
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll();
    }

    @Override
    public User findAllByID(String ID) {
        return userJpaRepository.findAllById(ID);
    }
}
