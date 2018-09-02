package com.cxyzj.cxyzjback.Data.User;

import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Catch.RoleList;
import lombok.Data;

/**
 * @Author 夏
 * @Date 09:38 2018/8/21
 */
@Data
public class UserSimple {

    private String user_id;
    private String nickname;
    private String head_url;
    private String role;
    private String introduce;
    private String gender;
    private int attentions;
    private int fans;
    private int articles;
    private int discussions;
    private int comments;

    public UserSimple(User user){
        this.user_id = user.getUserId();
        this.nickname = user.getNickname();
        this.head_url = user.getHeadUrl();
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
