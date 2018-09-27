package com.cxyzj.cxyzjback.Data.User;

import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Catch.RoleList;
import lombok.Data;

/**
 * @Package com.cxyzj.cxyzjback.Data.User
 * @Author Yaser
 * @Date 2018/08/11 16:46
 * @Description:
 */
@Data
public class UserBasic extends UserData {
    private String user_id;
    private String nickname;
    private String head_url;
    private String role;
    private String introduce;
    private int gender;
    private int attentions;
    private int articles;
    private int discussions;
    private int comments;
    private int fans;

    public UserBasic(User user) {
        this.user_id = user.getUserId();
        this.articles = user.getArticles();
        this.nickname = user.getNickname();
        this.comments = user.getComments();
        this.head_url = user.getHeadUrl();
        this.role = RoleList.getRoles().getRole(user.getRoleId());
        this.discussions = user.getDiscussions();
        this.attentions = user.getAttentions();
        this.gender = user.getGender();
        this.fans = user.getFans();
        this.introduce = user.getIntroduce();
    }
}
