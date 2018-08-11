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
    public static final int FAILURE = 400;
    //Token操作码101~110
    public static final int TOKEN_EXPIRED = 101;
    public static final int ILLEGAL_TOKEN = 103;
    public static final int ACCESS_DENIED = 104;
    //user操作码111~130
    public static final int INVALID_USER = 111;
    public static final int NONE_USER = 112;
    //article操作码131~160


    //discussion操作码161~190


    //course操作码301~330



    //其它操作码330~399
    public static final int NO_SUCH_FIELD = 331;

}
