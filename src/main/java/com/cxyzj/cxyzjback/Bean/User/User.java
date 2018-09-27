package com.cxyzj.cxyzjback.Bean.User;

import com.cxyzj.cxyzjback.Utils.Constant;
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
@Table(name = "User")
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
    private int gender;
    @Column(name = "email")
    private String email;
    @Column(name = "head_url")
    private String headUrl;
    @Column(name = "bg_url")
    private String bgUrl = "/img/user/background.jpeg";
    @Column(name = "introduce")
    private String introduce = "这个人很懒，连介绍都没有(￢︿̫̿￢☆)";
    @Column(name = "regist_date")
    private long registDate;
    @Column(name = "role_id")
    private int roleId = 1;
    @Column(name = "phone")
    private String phone;
    @Column(name = "theme_color")
    private String themeColor = "rgba(255,255,255,1)";
    @Column(name = "fans")
    private int fans = 0;
    @Column(name = "attentions")
    private int attentions = 0;
    @Column(name = "discussions")
    private int discussions = 0;
    @Column(name = "comments")
    private int comments = 0;
    @Column(name = "articles")
    private int articles = 0;
    @Column(name = "login_date")
    private long loginDate;
    @Column(name = "status_id")
    private int statusId = Constant.NORMAL;//默认为正常状态
}
