package com.cxyzj.cxyzjback.Service;

import com.cxyzj.cxyzjback.Bean.user.User;

public interface AuthService {
    User register(User user);

    String login(String email,String phone, String password) throws NoSuchFieldException;

    String refresh(String oldToken);
}
