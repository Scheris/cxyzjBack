package com.cxyzj.cxyzjback.Data.User;

import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Catch.RoleList;
import lombok.Data;

/**
 * @Author 夏
 * @Date 09:18 2018/8/21
 */
@Data
public class UserDetails {

    private String user_id;
    private String nickname;
    private String head_url;
    private String email;
    private String bg_url;
    private String regist_date;
    private String phone;
    private String theme_color;
    private String role;
    private String introduce;
    private String gender;
    private int attentions;
    private int fans;
    private int articles;
    private int discussions;
    private int comments;

    public UserDetails(User user){
        this.user_id = user.getUserId();
        this.nickname = user.getNickname();
        this.head_url = user.getHeadUrl();
        this.email = user.getEmail();
        this.bg_url = user.getBgUrl();
        this.regist_date = user.getRegistDate();
        this.phone = user.getPhone();
        this.theme_color = user.getThemeColor();
        this.role = RoleList.getRoles().getRole(user.getRoleId());
        this.introduce = user.getIntroduce();
        if(user.getGender() == 0){
            this.gender = "男";
        }else if(user.getGender() == 1){
            this.gender = "女";
        }else{
            this.gender = "保密";
        }
        this.attentions = user.getAttentions();
        this.fans = user.getFans();
        this.articles = user.getArticles();
        this.discussions = user.getDiscussions();
        this.comments = user.getComments();
    }


}
