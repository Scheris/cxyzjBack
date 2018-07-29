package com.cxyzj.cxyzjback.Repository;

import com.cxyzj.cxyzjback.Bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Package com.cxyzj.cxyzjback.Repository
 * @Author Yaser
 * @Date 2018/07/29 15:12
 * @Description:
 */
public interface UserJpaRepository extends JpaRepository<User, String> {
    User findAllById(String user_id);

    List<User> findAll();
}
