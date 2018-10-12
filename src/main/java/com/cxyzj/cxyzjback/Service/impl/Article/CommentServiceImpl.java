package com.cxyzj.cxyzjback.Service.impl.Article;

import com.cxyzj.cxyzjback.Bean.Article.*;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.Article.*;
import com.cxyzj.cxyzjback.Data.Other.PageData;
import com.cxyzj.cxyzjback.Data.User.OtherDetails;
import com.cxyzj.cxyzjback.Data.User.UserComment;
import com.cxyzj.cxyzjback.Repository.Article.*;
import com.cxyzj.cxyzjback.Repository.User.UserAttentionJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Article.CommentService;
import com.cxyzj.cxyzjback.Utils.Constant;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Auther: 夏
 * @DATE: 2018/9/6 15:23
 * @Description: 文章评论系统API
 * @checked false
 */

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentJpaRepository commentJpaRepository;

    private final CommentVoteJpaRepository commentVoteJpaRepository;

    private final ReplyJpaRepository replyJpaRepository;

    private final UserJpaRepository userJpaRepository;

    private final ArticleJpaRepository articleJpaRepository;

    private final UserAttentionJpaRepository userAttentionJpaRepository;

    private Comment comment;
    private CommentVote commentVote;
    private Response response;
    private String userId;
    private Reply reply;

    @Autowired
    public CommentServiceImpl(CommentJpaRepository commentJpaRepository, CommentVoteJpaRepository commentVoteJpaRepository, ReplyJpaRepository replyJpaRepository, UserJpaRepository userJpaRepository, ArticleJpaRepository articleJpaRepository, UserAttentionJpaRepository userAttentionJpaRepository) {
        this.commentJpaRepository = commentJpaRepository;
        this.commentVoteJpaRepository = commentVoteJpaRepository;
        this.replyJpaRepository = replyJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.articleJpaRepository = articleJpaRepository;
        this.userAttentionJpaRepository = userAttentionJpaRepository;
    }

    /**
     * @param text       评论
     * @param article_id 被评论的文章ID
     * @return 评论信息
     */
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

        //判断该文章是否有人评论过
        if(articleJpaRepository.findLevelsByArticleId(article_id) != 0) {
            //获取楼号数组，取得最大值
            int max = (int) Collections.max(commentJpaRepository.getLevel(article_id));
            comment.setLevel(max + 1);
        } else {
            comment.setLevel(1);
        }
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
     * @Description 获取文章回复列表
     */
    @Override
    public String getCommentList(String article_id, int pageNum) {

        response = new Response();
        Page<Comment> commentPage = getComment(pageNum, article_id);
        PageData pageData = new PageData(commentPage, pageNum);
        response.insert("list", getCommentList(commentPage.iterator()));
        response.insert(pageData);
        return response.sendSuccess();

    }

    @Override
    public String reply(String comment_id, String text, String discusser_id, String article_id) {
        response = new Response();
        reply = new Reply();
        comment = new Comment();

        if (commentJpaRepository.existsByCommentId(comment_id)) {
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
        } else {
            return response.sendFailure(Status.COMMENT_HAS_DELETE, "评论已删除");
        }
    }

    @Override
    public String commentDel(String commentId, String replyId) throws NoSuchFieldException {

        response = new Response();
        comment = new Comment();

        String targetId;
        if (commentId != null) {
            //判断reply表里面是否存在commentId
            if (replyJpaRepository.existsByCommentId(commentId)) {
                targetId = commentJpaRepository.findByCommentId(commentId).getTargetId();
                //如果存在，连带评论一起删除
                int count = replyJpaRepository.findCommentCount(commentId);//需要删除的回复的数量
                replyJpaRepository.deleteByCommentId(commentId);
                commentJpaRepository.deleteByCommentId(commentId);

                articleJpaRepository.updateCommentsByArticleId(articleJpaRepository.findCommentsByArticleId(targetId) - 1 - count, targetId);
                articleJpaRepository.updateLevelsByArticleId(articleJpaRepository.findLevelsByArticleId(targetId) - 1, targetId);
                return response.sendSuccess();
            } else {
                //如果不存在，就只删除comment数据
                if (commentJpaRepository.findByCommentId(commentId) != null) {
                    targetId = commentJpaRepository.findByCommentId(commentId).getTargetId();
                    commentJpaRepository.deleteByCommentId(commentId);
                    articleJpaRepository.updateCommentsByArticleId(articleJpaRepository.findCommentsByArticleId(targetId) - 1, targetId);
                    articleJpaRepository.updateLevelsByArticleId(articleJpaRepository.findLevelsByArticleId(targetId) - 1, targetId);
                    return response.sendSuccess();
                } else {
                    return response.sendFailure(Status.COMMENT_HAS_DELETE, "评论已删除");
                }
            }
        } else {
            if (replyId != null) {
                if (replyJpaRepository.findByReplyId(replyId) != null) {
                    replyJpaRepository.deleteByReplyId(replyId);
                    return response.sendSuccess();
                } else {
                    return response.sendFailure(Status.COMMENT_HAS_DELETE, "评论已删除");
                }
            } else {
                throw new NoSuchFieldException("评论和回复id不能同时为空");
            }
        }
    }

    @Override
    public String support(String commentId, String replyId) {

        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        commentVote = new CommentVote();

        if (commentVoteJpaRepository.findByUserIdAndTargetId(userId, commentId) == null
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
            } else if (replyJpaRepository.existsByReplyId(replyId)) {
                commentVote.setUserId(userId);
                commentVote.setTargetId(replyId);
                commentVote.setStatus(1);
                commentVoteJpaRepository.save(commentVote);
                //reply表中的support数+1
                int support = replyJpaRepository.findSupportByReplyId(replyId) + 1;
                replyJpaRepository.updateReplySupport(support, replyId);
                response.insert("support", replyJpaRepository.findSupportByReplyId(replyId));
                return response.sendSuccess();
            } else {
                return response.sendFailure(Status.COMMENT_HAS_DELETE, "评论已删除或不存在");
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

        if (!commentVoteJpaRepository.existsByUserIdAndTargetId(userId, commentId)
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
            } else if (replyJpaRepository.existsByReplyId(replyId)) {
                commentVote.setUserId(userId);
                commentVote.setTargetId(replyId);
                commentVote.setStatus(0);
                commentVoteJpaRepository.save(commentVote);
                //reply表中的object数+1
                int object = replyJpaRepository.findObjectByReplyId(replyId) + 1;
                replyJpaRepository.updateReplyObject(object, replyId);
                response.insert("object", replyJpaRepository.findObjectByReplyId(replyId));
                return response.sendSuccess();
            } else {
                return response.sendFailure(Status.COMMENT_HAS_DELETE, "评论已删除或不存在");
            }

        } else {
            return this.objectDel(commentId, replyId);
        }
    }

    @Override
    public String supportDel(String commentId, String replyId) {
        response = new Response();
        if (commentId != null) {
            if (commentVoteJpaRepository.existsByUserIdAndTargetId(userId, commentId)) {
                commentVoteJpaRepository.deleteByTargetId(commentId);
                int support = commentJpaRepository.findSupportByCommentId(commentId) - 1;
                commentJpaRepository.updateCommentSupport(support, commentId);
                response.insert("support", support);
                return response.sendSuccess();
            } else {
                return this.support(commentId, replyId);
            }
        } else {
            if (commentVoteJpaRepository.existsByUserIdAndTargetId(userId, replyId)) {
                commentVoteJpaRepository.deleteByTargetId(replyId);
                int support = replyJpaRepository.findSupportByReplyId(replyId) - 1;
                replyJpaRepository.updateReplySupport(support, replyId);
                response.insert("support", support);
                return response.sendSuccess();
            } else {
                return this.support(commentId, replyId);
            }
        }
    }

    @Override
    public String objectDel(String commentId, String replyId) {
        response = new Response();
        boolean status1 = commentVoteJpaRepository.existsByUserIdAndTargetId(userId, commentId);
        boolean status2 = commentVoteJpaRepository.existsByUserIdAndTargetId(userId, replyId);
        if (commentId != null) {
            if (status1) {
                commentVoteJpaRepository.deleteByTargetId(commentId);
                int object = commentJpaRepository.findObjectByCommentId(commentId) - 1;
                commentJpaRepository.updateCommentObject(object, commentId);
                response.insert("object", object);
                return response.sendSuccess();
            } else {
                return this.object(commentId, replyId);
            }
        } else {
            if (status2) {
                commentVoteJpaRepository.deleteByTargetId(replyId);
                int object = replyJpaRepository.findObjectByReplyId(replyId) - 1;
                replyJpaRepository.updateReplyObject(object, replyId);
                response.insert("object", object);
                return response.sendSuccess();
            } else {
                return this.object(commentId, replyId);
            }
        }
    }

    @Override
    public String replyList(String commentId, int pageNum) {
        response = new Response();
        Page<Reply> replyPage = getReply(pageNum, commentId);
        PageData pageData = new PageData(replyPage, pageNum);
        response.insert("list", getReplyList(replyPage.iterator()));
        response.insert(pageData);
        return response.sendSuccess();
    }

    private Page<Comment> getComment(int pageNum, String articleId){
        Sort sort = new Sort(Sort.DEFAULT_DIRECTION, "commentId");
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_COMMENT, sort);
        return commentJpaRepository.findAllByTargetId(pageable, articleId);
    }

    private Page<Reply> getReply(int pageNum, String commentId){
        Sort sort = new Sort(Sort.DEFAULT_DIRECTION, "replyId");
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_REPLY, sort);
        return replyJpaRepository.findAllByCommentId(pageable, commentId);
    }

    private Page<Reply> getChildren(String commentId){
        Sort sort = new Sort(Sort.DEFAULT_DIRECTION, "replyId");
        Pageable pageable = PageRequest.of(0, 3, sort);
        return replyJpaRepository.findAllByCommentId(pageable, commentId);
    }

    /**
     * @param ReplyIterator 获取回复列表迭代器
     * @return 回复列表信息
     */
    private List<ReplyList> getReplyList(Iterator<Reply> ReplyIterator) {
        ArrayList<ReplyBasic> replys = new ArrayList<>();//回复列表
        ArrayList<String> userIdList = new ArrayList<>();//用户id列表
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        while (ReplyIterator.hasNext()) {
            Reply reply = ReplyIterator.next();
            ReplyBasic replyBasic = new ReplyBasic(reply);
            replyBasic.setAllow_delete(userId.equals(reply.getReplier()));
            replyBasic.setAllow_vote(userId.equals(reply.getReplier()));
            replys.add(replyBasic);


            userIdList.add(reply.getReplier());//读取发出评论用户
        }

        ArrayList<User> userList = new ArrayList<>();

        for (String id : userIdList) {
            userList.add(userJpaRepository.findByUserId(id));//查询每一个user信息
        }
        ArrayList<ReplyList> resultList = new ArrayList<>();
        for (int i = 0; i < replys.size(); i++) {
            ReplyList replyList;


            User user = userList.get(i);//获取文章用户
            boolean status = false;
            if (userAttentionJpaRepository.existsByUserIdAndTargetUser(userId, user.getUserId())) {
                status = userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, user.getUserId()) == Constant.FOCUS ||
                        userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, user.getUserId()) == Constant.EACH;
            }
            OtherDetails otherDetails = new OtherDetails(user, status);//封装用户数据

            replyList = new ReplyList(replys.get(i),  otherDetails);//封装数据

            resultList.add(replyList);
        }
        return resultList;
    }

    /**
     * @param CommentIterator 获取评论列表迭代器
     * @return 评论列表信息
     */
    private List<CommentList> getCommentList(Iterator<Comment> CommentIterator) {
        ArrayList<CommentBasic> comments = new ArrayList<>();//文章评论
        ArrayList<String> userIdList = new ArrayList<>();//用户id列表
        ArrayList<String> replys = new ArrayList<>();//回复列表获取
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        while (CommentIterator.hasNext()) {
            Comment comment = CommentIterator.next();
            CommentBasic commentBasic = new CommentBasic(comment);
            commentBasic.setAllow_vote(userId.equals(comment.getDiscusser()));
            comments.add(commentBasic);

            userIdList.add(comment.getDiscusser());
            replys.add(comment.getCommentId());
        }

        ArrayList<User> userList = new ArrayList<>();
        ArrayList<List<ReplyList>> replyList = new ArrayList<>();

        for(String id : userIdList){
            userList.add(userJpaRepository.findByUserId(id));
        }

        for(String id : replys) {
            Page<Reply> replyPage = getChildren(id);
            replyList.add(getReplyList(replyPage.iterator()));
        }

        ArrayList<CommentList> resultList = new ArrayList<>();
        for(int i = 0; i<comments.size(); i++) {
            CommentList commentList;

            List<ReplyList> replyLists = replyList.get(i);

            User user = userList.get(i);
            boolean status = false;
            if (userAttentionJpaRepository.existsByUserIdAndTargetUser(userId, user.getUserId())) {
                status = userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, user.getUserId()) == Constant.FOCUS ||
                        userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, user.getUserId()) == Constant.EACH;
            }
            OtherDetails otherDetails = new OtherDetails(user, status);

            commentList = new CommentList(comments.get(i), otherDetails, replyLists);

            resultList.add(commentList);
        }

        return resultList;
    }

}
