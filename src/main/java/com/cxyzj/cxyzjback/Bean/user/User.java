package com.cxyzj.cxyzjback.Bean.user;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * @Package com.cxyzj.cxyzjback.Template
 * @Author Yaser
 * @Date 2018/08/04 15:57
 * @Description:
 */
@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")//自定义ID生成器
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "com.cxyzj.cxyzjback.Utils.SnowflakeIdGenerator")
    @Column(name = "user_id")
    private String userId;//小驼峰命名
    @Column(name = "password")
    private String password;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "gender")
    private String gender;
    @Column(name = "email")
    private String email;
    @Column(name = "head_url")
    private String headUrl;
    @Column(name = "bg_url")
    private String bgUrl;
    @Column(name = "introduce")
    private String introduce;
    @Column(name = "regist_data")
    private String registDate;
    @Column(name = "status")
    private int status;
    @Column(name = "phone")
    private String phone;
    @Column(name = "theme_color")
    private String themeColor;

}
