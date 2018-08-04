package com.cxyzj.cxyzjback.Bean.user;

import com.cxyzj.cxyzjback.Bean.Template;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * @Package com.cxyzj.cxyzjback.Template
 * @Author Yaser
 * @Date 2018/07/29 9:57
 * @Description:
 */
@Entity
@Data
@Table(name = "user")
public class User implements Template {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "com.cxyzj.cxyzjback.Utils.SnowflakeIdGenerator")
    @Column(name = "user_id")
    private String userId;
    @Column(name = "password")
    private String password = "sss";
    @Column(name = "nickname")
    private String nickname = "sss";
    @Column(name = "gender")
    private String gender = "sss";
    @Column(name = "email")
    private String email = "sss";
    @Column(name = "head_url")
    private String headUrl = "sss";
    @Column(name = "bg_url")
    private String bgUrl = "sss";
    @Column(name = "introduce")
    private String introduce = "sss";
    @Column(name = "regist_data")
    private String registDate = "sss";
    @Column(name = "status")
    private int status = 1;
    @Column(name = "phone")
    private String phone = "sss";
    @Column(name = "theme_color")
    private String themeColor = "ss";

    @Override
    public String getClassName() {
        return "user";
    }
}
