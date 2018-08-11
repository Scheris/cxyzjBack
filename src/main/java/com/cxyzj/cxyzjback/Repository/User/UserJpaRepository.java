package com.cxyzj.cxyzjback.Repository.User;

import com.cxyzj.cxyzjback.Bean.User.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Package com.cxyzj.cxyzjback.Repository
 * @Author Yaser
 * @Date 2018/07/29 15:12
 * @Description:
 */
public interface UserJpaRepository extends JpaRepository<User, String> {
    User findByEmailAndPassword(String email, String password);

    User findByPhoneAndPassword(String Phone, String password);

    User findByuserId(String userId);


    List<User> findAll();


}
