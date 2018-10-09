package com.cxyzj.cxyzjback.Utils;

/**
 * @Package com.cxyzj.cxyzjback.Utils
 * @Author Yaser
 * @Date 2018/09/24 19:54
 * @Description: 常量表
 */
public final class Constant {
    public static final int PAGE_ATTENTION_USER = 9; //一页的用户数量
    public static final int PAGE_ARTICLE = 10; //一页的文章数量
    public static final int PAGE_COMMENT = 10; //一页的评论的数量
    public static final int PAGE_REPLY = 5; //一页的回复数量

    //数据库status表的信息
    public static final int OBJECT = 0;//反对
    public static final int SUPPORT = 1;//支持
    public static final int DRAFT = 100;//草稿
    public static final int PUBLISH = 101;//已发布
    public static final int CHECKING = 102;//审核中
    public static final int BANNED = 103;//封禁
    public static final int NORMAL = 104;//正常
    public static final int FOCUS = 201;//关注
    public static final int FOLLOWED = 202;//被关注
    public static final int EACH = 203;//互相关注
}
