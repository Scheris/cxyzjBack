package com.cxyzj.cxyzjback.Utils;

/**
 * @Package com.cxyzj.cxyzjback.Utils
 * @Author Yaser
 * @Date 2018/08/04 20:09
 * @Description: 所有的状态码，后期可进行扩展
 */
public final class Status {
    //操作成功
    public static final int SUCCESS = 200;
    //操作失败
    public static final int FAILURE = 400;
    //Token操作码101~110
    public static final int TOKEN_EXPIRED = 101;//token已过期
    public static final int ILLEGAL_TOKEN = 103;//非法的token
    public static final int ACCESS_DENIED = 104;//访问拒绝
    //user操作码111~130
    public static final int INVALID_USER = 111;//非法的用户
    public static final int NONE_USER = 112;//没有该用户
    public static final int CODE_ERROR = 113;//验证码错误
    public static final int PHONE_HAS_REGISTER = 114;//该手机号已被注册
    public static final int EMAIL_HAS_REGISTER = 115;//该邮箱已注册
    public static final int NICKNAME_EXIST = 116;//昵称已存在
    public static final int WRONG_PASSWORD = 117;//密码错误
    public static final int CODE_SEND_FAILURE = 118;//验证码发送失败
    public static final int OUT_OF_TIME = 119;//验证过期
    public static final int USER_HAS_FOLLOWED = 120;
    //article操作码131~160


    //discussion操作码161~190


    //course操作码301~330


    //其它操作码330~399
    public static final int NO_SUCH_FIELD = 331;

    public static final int REDIS_NOT_START = 404;

}
