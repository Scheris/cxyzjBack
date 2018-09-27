package com.cxyzj.cxyzjback.Data.User;

import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Catch.RoleList;
import lombok.Data;


/**
 * @Auther: 夏
 * @DATE: 2018/9/6 17:03
 * @Description:
 */
@Data
public class UserComment extends UserData{

    private String user_id;
    private String nickname;
    private String head_url;
    private String role;
    private String introduce;
    private int gender;
    private boolean is_followed;
    private int attention;
    private int fans;
    private int status_id;


    public UserComment(User user){
        this.user_id = user.getUserId();
        this.nickname = user.getNickname();
        this.head_url = user.getHeadUrl();
        this.role = RoleList.getRoles().getRole(user.getRoleId());
        this.gender = user.getGender();
        this.introduce = user.getIntroduce();
        this.is_followed = false;
        this.attention = user.getAttentions();
        this.fans = user.getFans();
        this.status_id = user.getStatusId();

    }

}