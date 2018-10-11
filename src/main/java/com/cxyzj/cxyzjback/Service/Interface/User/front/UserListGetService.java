package com.cxyzj.cxyzjback.Service.Interface.User.front;


/**
 * @Author 夏
 * @Date 15:50 2018/8/30
 */
public interface UserListGetService {
    String getAttentionList(String userId,int pageNum);

    String getFansList(String userId,int pageNum);

    String getArticleList(String userId, int pageNum);

    String userComments(String user_id, int pageNum);
}