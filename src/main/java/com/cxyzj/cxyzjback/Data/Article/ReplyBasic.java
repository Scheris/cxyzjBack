package com.cxyzj.cxyzjback.Data.Article;

import com.cxyzj.cxyzjback.Bean.Article.Reply;
import lombok.Data;

/**
 * @Auther: 夏
 * @DATE: 2018/9/7 10:11
 * @Description:
 */
@Data
public class ReplyBasic implements com.cxyzj.cxyzjback.Data.Data {

    private String commentId;
    private String replyId;
    private String text;
    private long createTime;
    private int support;
    private boolean allow_vote;
    private boolean is_support;
    private boolean is_obj;
    private boolean allow_delete;

    public ReplyBasic(Reply reply){
        this.commentId = reply.getCommentId();
        this.replyId = reply.getReplyId();
        this.text = reply.getText();
        this.createTime = reply.getReplyTime();
        this.support = reply.getSupport();
        this.allow_vote = false;
        this.is_support = false;
        this.is_obj = false;
        this.allow_delete = true;
    }

    @Override
    public String getName() {
        return "reply";
    }
}
