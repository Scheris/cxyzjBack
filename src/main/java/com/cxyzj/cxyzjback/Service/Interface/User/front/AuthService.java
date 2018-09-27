package com.cxyzj.cxyzjback.Service.Interface.User.front;

import com.cxyzj.cxyzjback.Bean.User.User;

/**
 * @Author 夏
 * @Date 15:46 2018/8/15
 */
public interface AuthService {

    String login(String email, String phone, String password) throws NoSuchFieldException;

    String register(String nickname, String email, String password, int gender, String phone, String headUrl);

    String sendCode(String email, String phone) throws Exception;

    String verifyCode(String phone, String email, String code) throws NoSuchFieldException;

    String loginCode(String phone, String code) ;

    String forgetPassword(String email, String phone, String password, String code) throws NoSuchFieldException;

    String existUser(String email, String phone, String nickname) throws NoSuchFieldException;
}
