package com.cxyzj.cxyzjback.Service.Interface.User.front;

import com.cxyzj.cxyzjback.Bean.User.User;

/**
 * @Author 夏
 * @Date 10:15 2018/8/25
 */
public interface UserInfoService {

    String detailsOwn();

    String simpleOwn();

    String detailsOther(String otherId);

    String updateNickname(String nickname);

    String updateHead(String headUrl);

    String updateGender(String gender);

    String updateIntroduce(String introduce);

    String updateThemeColor(String themeColor);

    String updateBg(String bgUrl);

    String sendCode(String verifyType);

    String verify(String code);

    String updatePassword(String password, String user_id);

    String updatePhone(String phone, String user_id);

    String updateEmail(String email, String user_id);

    String refreshToken();

    String simpleOther(String otherId);

    String follow(String targetId);

    String delFollow(String targetId);
}
