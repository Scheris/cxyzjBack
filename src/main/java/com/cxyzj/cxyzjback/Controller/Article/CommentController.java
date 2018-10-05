package com.cxyzj.cxyzjback.Controller.Article;

import com.cxyzj.cxyzjback.Service.Interface.Article.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: 夏
 * @DATE: 2018/9/6 15:17
 * @Description:
 */

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1/article")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * @Description 发表评论
     */
    @PostMapping(value = "/comment")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String comment(@RequestParam String text, @RequestParam String article_id) {
        return commentService.comment(text, article_id);
    }

    /**
     * @Description 获取评论列表
     */
    @GetMapping(value = "/comment_list/{article_id}/{page_num}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getCommentList(@PathVariable(name = "article_id") String article_id,
                                 @PathVariable(name = "page_num") int pageNum){
        return commentService.getCommentList(article_id, pageNum);
    }

    /**
     * @Description 回复评论
     */
    @PostMapping(value = "/comment/reply")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String reply(@RequestParam String comment_id, @RequestParam String text, @RequestParam String discusser_id,
                        @RequestParam String article_id){
        return commentService.reply(comment_id, text, discusser_id, article_id);
    }

    /**
     * @Description 删除评论
     */
    @DeleteMapping(value = "/comment")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String commentDel(@RequestParam(required = false) String comment_id,
                             @RequestParam(required = false) String reply_id) throws NoSuchFieldException {
        return commentService.commentDel(comment_id, reply_id);
    }

    /**
     * @Description 支持评论
     */
    @PostMapping(value = "/comment/support")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String support(@RequestParam(required = false) String comment_id, @RequestParam(required = false) String reply_id){
        return commentService.support(comment_id, reply_id);
    }

    /**
     * @Description 反对评论
     */
    @PostMapping(value = "/comment/object")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String object(@RequestParam(required = false) String comment_id, @RequestParam(required = false) String reply_id) {
        return commentService.object(comment_id, reply_id);
    }

    /**
     * @Description 取消支持评论
     */
    @DeleteMapping(value = "/comment/support")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String supportDel(@RequestParam(required = false) String comment_id, @RequestParam(required = false) String reply_id){
        return commentService.supportDel(comment_id, reply_id);
    }

    /**
     * @Description 取消反对评论
     */
    @DeleteMapping(value = "/comment/object")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String objectDel(@RequestParam(required = false) String comment_id, @RequestParam(required = false) String reply_id){
        return commentService.objectDel(comment_id, reply_id);
    }

}
