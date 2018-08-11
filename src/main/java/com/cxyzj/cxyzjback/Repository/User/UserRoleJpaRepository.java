package com.cxyzj.cxyzjback.Repository.User;

import com.cxyzj.cxyzjback.Bean.User.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRoleJpaRepository extends JpaRepository<Role, Integer> {
    List<Role> findAll();
}
