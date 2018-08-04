package com.cxyzj.cxyzjback.Utils;

import com.cxyzj.cxyzjback.Bean.Template;
import com.cxyzj.cxyzjback.Bean.user.User;

/**
 * @Package com.cxyzj.cxyzjback.Utils
 * @Author Yaser
 * @Date 2018/08/04 12:11
 * @Description: 用于用户安全验证
 */
public class Token  {
    public static TokenResult createToken(User user) {//对于不同的用户生成具体的token
        //todo 待实现
        TokenResult tokenResult = new TokenResult();
        tokenResult.setStatus(true);
        tokenResult.setToken(user.getUserId() + "," + user.getPassword());
        return tokenResult;
    }

    public static TokenResult verify(TokenResult tokenResult) {//验证接收到的token
        //todo 待实现
        return new TokenResult();
    }


}
