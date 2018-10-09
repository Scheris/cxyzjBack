package com.cxyzj.cxyzjback.Service.Interface.Article;

/**
 * @Auther: 夏
 * @DATE: 2018/9/6 15:24
 * @Description:
 */
public interface CommentService {
    String comment(String text, String article_id);

    String getCommentList(String article_id, int pageNum);

    String reply(String comment_id, String text, String discusser_id, String article_id);

    String commentDel(String commentId, String replyId) throws NoSuchFieldException;

    String support(String commentId, String replyId);

    String object(String comment_id, String reply_id);

    String supportDel(String comment_id, String reply_id);

    String objectDel(String comment_id, String reply_id);

    String replyList(String commentId, int pageNum);
}
