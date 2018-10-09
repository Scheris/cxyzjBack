package com.cxyzj.cxyzjback.Data.Article;

import com.cxyzj.cxyzjback.Data.User.OtherDetails;
import lombok.Data;

import java.util.List;

/**
 * @Auther: 夏
 * @DATE: 2018/10/9 10:19
 * @Description:
 */

@Data
public class CommentList implements com.cxyzj.cxyzjback.Data.Data {

    private CommentBasic comment;
    private OtherDetails user;
    private List<ReplyList> replyList;

    public CommentList(CommentBasic comment, OtherDetails user, List<ReplyList> replyList) {
        this.comment = comment;
        this.user = user;
        this.replyList = replyList;
    }


    @Override
    public String getName() {
        return "list";
    }
}
