package com.cxyzj.cxyzjback.Data.Article;

import com.cxyzj.cxyzjback.Bean.Article.Comment;
import lombok.Data;

/**
 * @Auther: 夏
 * @DATE: 2018/9/6 16:14
 * @Description:
 */

@Data
public class CommentBasic implements com.cxyzj.cxyzjback.Data.Data {

    private String comment_id;
    private String text;
    private long createTime;
    private int support;
    private boolean allow_vote;
    private boolean is_support;
    private boolean is_obj;
    private int level;
    private int children;

    public CommentBasic(Comment comment){
        this.comment_id = comment.getCommentId();
        this.text = comment.getText();
        this.createTime = comment.getCreateTime();
        this.support = comment.getSupport();
        this.allow_vote = true;
        this.is_obj = false;
        this.is_support = false;
        this.level = comment.getLevel();
        this.children = comment.getChildren();
    }


    @Override
    public String getName() {
        return "comment";
    }
}
