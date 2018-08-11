package com.cxyzj.cxyzjback.Service.Interface.User;

import com.cxyzj.cxyzjback.Bean.User.User;

public interface AuthService {
    User register(User user);

    String login(String email,String phone, String password) throws NoSuchFieldException;

    String refresh(String oldToken);
}
