package com.cxyzj.cxyzjback.Service.impl.Article;

import com.cxyzj.cxyzjback.Bean.Article.Comment;
import com.cxyzj.cxyzjback.Bean.Article.CommentVote;
import com.cxyzj.cxyzjback.Bean.Article.Reply;
import com.cxyzj.cxyzjback.Bean.PageBean;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.Article.CommentBasic;
import com.cxyzj.cxyzjback.Data.Article.ReplyBasic;
import com.cxyzj.cxyzjback.Data.User.UserComment;
import com.cxyzj.cxyzjback.Repository.Article.ArticleJpaRepository;
import com.cxyzj.cxyzjback.Repository.Article.CommentJpaRepository;
import com.cxyzj.cxyzjback.Repository.Article.CommentVoteJpaRepository;
import com.cxyzj.cxyzjback.Repository.Article.ReplyJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Article.CommentService;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Auther: 夏
 * @DATE: 2018/9/6 15:23
 * @Description:
 */

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentJpaRepository commentJpaRepository;

    @Autowired
    private CommentVoteJpaRepository commentVoteJpaRepository;

    @Autowired
    private ReplyJpaRepository replyJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ArticleJpaRepository articleJpaRepository;
    private Comment comment;
    private CommentVote commentVote;
    private Response response;
    private String userId;
    private Reply reply;
    @Override
    public String comment(String text, String article_id) {
        response = new Response();
        comment = new Comment();
        long create_time = System.currentTimeMillis();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        comment.setDiscusser(userId);
        comment.setText(text);
        comment.setCreateTime(create_time);
        comment.setTargetId(article_id);
        comment.setMode("article");

        //获取楼号数组，取得最大值
        int max = (int) Collections.max(commentJpaRepository.getLevel(article_id));
        comment.setLevel(max+1);
        commentJpaRepository.save(comment);
        articleJpaRepository.updateCommentsByArticleId(articleJpaRepository.findCommentsByArticleId(article_id) + 1, article_id);
        articleJpaRepository.updateLevelsByArticleId(articleJpaRepository.findLevelsByArticleId(article_id) + 1, article_id);

        UserComment userComment = new UserComment(userJpaRepository.findByUserId(userId));

        CommentBasic commentBasic = new CommentBasic(comment);
        commentBasic.setAllow_vote(false);
        response.insert("comment", commentBasic);
        response.insert("user", userComment);
        return response.sendSuccess();
    }


    /**
     * @param article_id
     * @param pageNum
     * @Description 还没写好，待定。。。
     */
    @Override
    public String getCommentList(String article_id, int pageNum) {

        List<CommentBasic> commentLists = new ArrayList<>();
        List<UserComment> userComments = new ArrayList<>();
        List<UserComment> userReplies = new ArrayList<>();
        response = new Response();
        comment = new Comment();

        List<Comment> commentList = commentJpaRepository.findListByTargetId(article_id);

        int totalRecord = commentList.size();
        PageBean pb = new PageBean();
        pb.PageBean(pageNum, 10, totalRecord);
        int startIndex = pb.getStartIndex();

        Comment[] comments = commentList.toArray(new Comment[0]);
        pb.setList(commentJpaRepository.findAll(startIndex, 10));
        pb.setPageNum(pageNum);

        List<Reply> replies = new ArrayList<>();
        List[] replyLists = new List[0];

        Reply[] reply1 = new Reply[0];
        for(int i = 0; i<comments.length; i++){
            comment = commentJpaRepository.findByCommentId(comments[i].getCommentId());
            User user = userJpaRepository.findByUserId(comments[i].getDiscusser());
            reply1 = replyJpaRepository.findByUserIdAndTargetId(comments[i].getDiscusser(), comments[i].getTargetId());

            if(comment != null && reply1 == null){

            }

            UserComment userComment = new UserComment(user);
            CommentBasic commentBasic = new CommentBasic(comment);
            if(commentVoteJpaRepository.findByUserIdAndTargetId(userId, comment.getDiscusser()) != null){
                commentBasic.setAllow_vote(false);
                userComments.add(userComment);
                commentLists.add(commentBasic);
            }else {
                userComments.add(userComment);
                commentLists.add(commentBasic);
            }
        }

        for (Reply aReply1 : reply1) {
            replies.add(aReply1);
            UserComment userReply = new UserComment(userJpaRepository.findByUserId(aReply1.getReplier()));
            userReplies.add(userReply);
            replyLists = new List[]{replies, userReplies};
        }

        List[] list = new List[]{commentLists,userComments, Arrays.asList(replyLists)};
        response.insert(list);
        return response.sendSuccess();
    }

    @Override
    public String reply(String comment_id, String text, String discusser_id, String article_id) {
        response = new Response();
        reply = new Reply();
        comment = new Comment();

        if(commentJpaRepository.existsByCommentId(comment_id)){
            userId = SecurityContextHolder.getContext().getAuthentication().getName();
            long createTime = System.currentTimeMillis();
            reply.setCommentId(comment_id);
            reply.setText(text);
            reply.setReplyTime(createTime);
            reply.setReplier(userId);
            reply.setUserId(discusser_id);
            reply.setTargetId(article_id);
            reply.setMode("article");

            replyJpaRepository.save(reply);

            commentJpaRepository.updateChildren(commentJpaRepository.findChildren(comment_id) + 1, comment_id);
            articleJpaRepository.updateCommentsByArticleId(articleJpaRepository.findCommentsByArticleId(article_id) + 1, article_id);

            ReplyBasic replyBasic = new ReplyBasic(reply);
            response.insert("reply", replyBasic);

            UserComment userComment = new UserComment(userJpaRepository.findByUserId(userId));
            response.insert("user", userComment);

            return response.sendSuccess();
        }else {
            return response.sendFailure(Status.COMMENT_HAS_DELETE, "评论已删除");
        }
    }

    @Override
    public String commentDel(String commentId, String replyId) throws NoSuchFieldException {

        response = new Response();
        comment = new Comment();

        String targetId;
        if(commentId != null){
            //判断reply表里面是否存在commentId
            if(replyJpaRepository.existsByCommentId(commentId)){
                targetId = commentJpaRepository.findByCommentId(commentId).getTargetId();
                //如果存在，连带评论一起删除
                int count = replyJpaRepository.findCommentCount(commentId);//需要删除的回复的数量
                replyJpaRepository.deleteByCommentId(commentId);
                commentJpaRepository.deleteByCommentId(commentId);

                articleJpaRepository.updateCommentsByArticleId(articleJpaRepository.findCommentsByArticleId(targetId) - 1 - count, targetId);
                articleJpaRepository.updateLevelsByArticleId(articleJpaRepository.findLevelsByArticleId(targetId) - 1, targetId);
                return response.sendSuccess();
            }else {
                //如果不存在，就只删除comment数据
                if(commentJpaRepository.findByCommentId(commentId) != null) {
                    targetId = commentJpaRepository.findByCommentId(commentId).getTargetId();
                    commentJpaRepository.deleteByCommentId(commentId);
                    articleJpaRepository.updateCommentsByArticleId(articleJpaRepository.findCommentsByArticleId(targetId) - 1, targetId);
                    articleJpaRepository.updateLevelsByArticleId(articleJpaRepository.findLevelsByArticleId(targetId) - 1, targetId);
                    return response.sendSuccess();
                }else {
                    return response.sendFailure(Status.COMMENT_HAS_DELETE,"评论已删除");
                }
            }
        }else {
            if(replyId != null){
                if(replyJpaRepository.findByReplyId(replyId) != null) {
                    replyJpaRepository.deleteByReplyId(replyId);
                    return response.sendSuccess();
                }else {
                    return response.sendFailure(Status.COMMENT_HAS_DELETE,"评论已删除");
                }
            }else {
                throw new NoSuchFieldException("评论和回复id不能同时为空");
            }
        }
    }

    @Override
    public String support(String commentId, String replyId) {

        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        commentVote = new CommentVote();

        if(commentVoteJpaRepository.findByUserIdAndTargetId(userId, commentId) == null
                && commentVoteJpaRepository.findByUserIdAndTargetId(userId, replyId) == null) {
            if (commentJpaRepository.existsByCommentId(commentId)) {
                commentVote.setUserId(userId);
                commentVote.setTargetId(commentId);
                commentVote.setStatus(1);
                commentVoteJpaRepository.save(commentVote);
                //comment表中的support数+1
                int support = commentJpaRepository.findSupportByCommentId(commentId) + 1;
                commentJpaRepository.updateCommentSupport(support, commentId);
                response.insert("support", commentJpaRepository.findSupportByCommentId(commentId));
                return response.sendSuccess();
            }else if(replyJpaRepository.existsByReplyId(replyId)) {
                commentVote.setUserId(userId);
                commentVote.setTargetId(replyId);
                commentVote.setStatus(1);
                commentVoteJpaRepository.save(commentVote);
                //reply表中的support数+1
                int support = replyJpaRepository.findSupportByReplyId(replyId) + 1;
                replyJpaRepository.updateReplySupport(support, replyId);
                response.insert("support", replyJpaRepository.findSupportByReplyId(replyId));
                return response.sendSuccess();
            }else {
                return response.sendFailure(Status.COMMENT_HAS_DELETE,"评论已删除或不存在");
            }
        } else {
            return this.supportDel(commentId, replyId);
        }
    }

    @Override
    public String object(String commentId, String replyId) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        commentVote = new CommentVote();

        if(!commentVoteJpaRepository.existsByUserIdAndTargetId(userId, commentId)
                && !commentVoteJpaRepository.existsByUserIdAndTargetId(userId, replyId)) {
            if (commentJpaRepository.existsByCommentId(commentId)) {
                commentVote.setUserId(userId);
                commentVote.setTargetId(commentId);
                commentVote.setStatus(0);
                commentVoteJpaRepository.save(commentVote);
                //comment表中的object数+1
                int object = commentJpaRepository.findObjectByCommentId(commentId) + 1;
                commentJpaRepository.updateCommentObject(object, commentId);
                response.insert("object", commentJpaRepository.findObjectByCommentId(commentId));
                return response.sendSuccess();
            }else if(replyJpaRepository.existsByReplyId(replyId)) {
                commentVote.setUserId(userId);
                commentVote.setTargetId(replyId);
                commentVote.setStatus(0);
                commentVoteJpaRepository.save(commentVote);
                //reply表中的object数+1
                int object = replyJpaRepository.findObjectByReplyId(replyId) + 1;
                replyJpaRepository.updateReplyObject(object, replyId);
                response.insert("object", replyJpaRepository.findObjectByReplyId(replyId));
                return response.sendSuccess();
            }else {
                return response.sendFailure(Status.COMMENT_HAS_DELETE,"评论已删除或不存在");
            }

        } else {
            return this.objectDel(commentId, replyId);
        }
    }

    @Override
    public String supportDel(String commentId, String replyId) {
        response = new Response();
        if(commentId != null){
            if(commentVoteJpaRepository.existsByUserIdAndTargetId(userId, commentId)) {
                commentVoteJpaRepository.deleteByTargetId(commentId);
                int support = commentJpaRepository.findSupportByCommentId(commentId) - 1;
                commentJpaRepository.updateCommentSupport(support, commentId);
                response.insert("support", support);
                return response.sendSuccess();
            }else {
                return this.support(commentId, replyId);
            }
        }else {
            if(commentVoteJpaRepository.existsByUserIdAndTargetId(userId, replyId)) {
                commentVoteJpaRepository.deleteByTargetId(replyId);
                int support = replyJpaRepository.findSupportByReplyId(replyId) - 1;
                replyJpaRepository.updateReplySupport(support, replyId);
                response.insert("support", support);
                return response.sendSuccess();
            }else {
                return this.support(commentId, replyId);
            }
        }
    }

    @Override
    public String objectDel(String commentId, String replyId) {
        response = new Response();
        boolean status1 = commentVoteJpaRepository.existsByUserIdAndTargetId(userId, commentId);
        boolean status2 = commentVoteJpaRepository.existsByUserIdAndTargetId(userId, replyId);
        if(commentId != null){
            if (status1){
                commentVoteJpaRepository.deleteByTargetId(commentId);
                int object = commentJpaRepository.findObjectByCommentId(commentId) - 1;
                commentJpaRepository.updateCommentObject(object, commentId);
                response.insert("object", object);
                return response.sendSuccess();
            }else {
                return this.object(commentId, replyId);
            }
        }else {
            if(status2){
                commentVoteJpaRepository.deleteByTargetId(replyId);
                int object = replyJpaRepository.findObjectByReplyId(replyId) - 1;
                replyJpaRepository.updateReplyObject(object, replyId);
                response.insert("object", object);
                return response.sendSuccess();
            }else {
                return this.object(commentId, replyId);
            }
        }
    }
}
