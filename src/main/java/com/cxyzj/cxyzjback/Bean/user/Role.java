package com.cxyzj.cxyzjback.Bean.user;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Package com.cxyzj.cxyzjback.Bean.user
 * @Author Yaser
 * @Date 2018/08/09 15:52
 * @Description:
 */
@Data
@Entity
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "role_id")
    private int roleId;
    @Column(name = "role")
    private String role;
    @Column(name = "role_info")
    private String roleInfo;


}
