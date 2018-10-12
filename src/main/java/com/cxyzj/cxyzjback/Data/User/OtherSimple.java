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
    private int gender;

    public OtherSimple(User user) {

        this.user_id = user.getUserId();
        this.nickname = user.getNickname();
        this.head_url = user.getHeadUrl();
        this.gender = user.getGender();

    }

}
