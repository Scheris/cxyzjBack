package com.cxyzj.cxyzjback.Repository;

import com.cxyzj.cxyzjback.Bean.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRoleJpaRepository extends JpaRepository<Role, Integer> {
    List<Role> findAll();
}
