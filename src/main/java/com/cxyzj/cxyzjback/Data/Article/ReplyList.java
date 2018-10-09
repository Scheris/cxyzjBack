package com.cxyzj.cxyzjback.Data.Article;


import com.cxyzj.cxyzjback.Data.User.OtherDetails;
import lombok.Data;

/**
 * @Auther: 夏
 * @DATE: 2018/10/9 09:48
 * @Description:
 */

@Data
public class ReplyList implements com.cxyzj.cxyzjback.Data.Data {

    private ReplyBasic reply;
    private OtherDetails user;

    public ReplyList(ReplyBasic reply, OtherDetails user) {
        this.reply = reply;
        this.user = user;
    }

    @Override
    public String getName() {
        return "children";
    }

}
