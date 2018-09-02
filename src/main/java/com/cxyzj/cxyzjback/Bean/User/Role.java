package com.cxyzj.cxyzjback.Bean.User;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Package com.cxyzj.cxyzjback.Bean.User
 * @Author Yaser
 * @Date 2018/08/09 15:52
 * @Description: 用户角色表
 */
@Data
@Entity
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "role_id")
    private int roleId = 0;
    @Column(name = "role")
    private String role;
    @Column(name = "role_info")
    private String roleInfo;
}
