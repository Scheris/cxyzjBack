package com.cxyzj.cxyzjback.Data.User;

import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Catch.RoleList;
import lombok.Data;

/**
 * @Author 夏
 * @Date 17:18 2018/8/29
 */

@Data
public class OtherSimple extends UserData {

    private String user_id;
    private String nickname;
    private String head_url;
    private String role;
    private String introduce;
    private int gender;
    private int attentions;
    private int fans;
    private boolean is_followed;

    public OtherSimple(User user) {

        this.user_id = user.getUserId();
        this.nickname = user.getNickname();
        this.head_url = user.getHeadUrl();
        this.role = RoleList.getRoles().getRole(user.getRoleId());
        this.introduce = user.getIntroduce();
        this.gender = user.getGender();
        this.attentions = user.getAttentions();
        this.fans = user.getFans();
        this.is_followed = false;

    }

}
