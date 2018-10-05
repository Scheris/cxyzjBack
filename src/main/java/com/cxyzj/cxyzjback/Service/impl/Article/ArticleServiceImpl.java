package com.cxyzj.cxyzjback.Service.impl.Article;

import com.cxyzj.cxyzjback.Bean.Article.*;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.Article.ArticleBasic;
import com.cxyzj.cxyzjback.Data.Article.ArticleTypeBasic;
import com.cxyzj.cxyzjback.Data.User.UserBasic;
import com.cxyzj.cxyzjback.Repository.Article.*;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Article.ArticleService;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: 夏
 * @DATE: 2018/9/12 10:10
 * @Description:
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleJpaRepository articleJpaRepository;

    @Autowired
    private ArticleTypeJpaRepository articleTypeJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ArticleCollectionJpaRepository articleCollectionJpaRepository;

    @Autowired
    private ReplyJpaRepository replyJpaRepository;

    @Autowired
    private CommentJpaRepository commentJpaRepository;

    @Autowired
    private CommentVoteJpaRepository commentVoteJpaRepository;

    private Response response;
    private String userId;

    /**
     *
     * @param title 文章标题
     * @param text 文章内容
     * @param type_id 文章类型
     * @param article_sum 文章概要
     * @param thumbnail 缩略图
     * @param status_id 文章状态
     * @param user_id 用户id
     * @return 文章ID
     * @checked true
     */
    @Override
    public String writeArticle(String title, String text, String type_id, String article_sum,
                               String thumbnail, int status_id, String user_id) {
        response = new Response();
        Article article = new Article();
        article.setTitle(title);
        article.setText(text);
        article.setTypeId(type_id);
        article.setArticleSum(article_sum);
        article.setThumbnail(thumbnail);
        article.setStatus_id(status_id);
        article.setUserId(user_id);
        article.setUpdateTime(System.currentTimeMillis());

        article = articleJpaRepository.save(article);
        userJpaRepository.increaseArticlesByUserId(1, userId);//文章数+1
        response.insert("article_id", article.getArticleId());
        return response.sendSuccess();
    }

    /**
     * @return 文章类型数据
     * @checked true
     * @Description: 以后可进行优化点：类型一般来说不会轻易修改，所以可以做缓存处理，不用每次都从数据库读取，
     * 但考虑到后台管理员还是可能会增加新的类型，所以如果后台新增了新的类型，则重新读取type数据并缓存处理。
     */
    @Override
    public String getTypes() {
        response = new Response();
        List<ArticleType> articleTypes = articleTypeJpaRepository.findAll();
        List<ArticleTypeBasic> articleTypeList = new ArrayList<>();
        for (ArticleType articleType : articleTypes) {
            ArticleTypeBasic articleTypeBasic = new ArticleTypeBasic(articleType);
            articleTypeList.add(articleTypeBasic);
        }
        response.insert("type", articleTypeList);
        return response.sendSuccess();
    }

    /**
     * @param articleId 文章ID
     * @return 文章数据
     * @checked true
     * @Description: 以后可进行优化点：对于每一篇未缓存处理的文章，在初次访问可以做一个有时间限制(例如3分钟)的缓存，
     * 之后，每有一个人访问，这个时间就增加一点（例如增加1分钟），
     * 如果没有人访问则时间到了之后缓存自动删除
     */
    @Override
    public String articleDetails(String articleId) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Article article = articleJpaRepository.findByArticleId(articleId);

        ArticleBasic articleBasic = new ArticleBasic(article);
        User user = userJpaRepository.findByUserId(article.getUserId());
        ArticleType articleType = articleTypeJpaRepository.findByTypeId(article.getTypeId());

        if (userId.equals(article.getUserId())) {
            //是作者
            articleBasic.set_author(true);
            articleBasic.setAllow_delete(true);
            articleBasic.setAllow_edit(true);
            articleBasic.set_collected(false);
            response.insert("article", articleBasic);

        } else {
            //不是作者
            //如果已收藏该文章
            if (articleCollectionJpaRepository.existsByArticleIdAndUserId(articleId, userId)) {
                articleBasic.set_collected(true);
                response.insert("article", articleBasic);
            }
        }

        response.insert("type", new ArticleTypeBasic(articleType));
        response.insert("user", new UserBasic(user));
        return response.sendSuccess();
    }

    /**
     * @param articleId 文章ID
     * @return 是否收藏成功
     * @checked true
     */
    @Override
    public String collect(String articleId) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!articleCollectionJpaRepository.existsByArticleIdAndUserId(articleId, userId)) {
            ArticleCollection articleCollection = new ArticleCollection();
            articleCollection.setArticleId(articleId);
            articleCollection.setUserId(userId);
            articleCollectionJpaRepository.save(articleCollection);
            articleJpaRepository.increaseCollectionsByArticleId(articleId);
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.ARTICLE_HAS_COLLECTED, "该文章已被收藏过了！");
        }
    }

    /**
     * @param articleId 文章id
     * @return 是否取消收藏成功
     * @checked true
     */
    @Override
    public String collectDel(String articleId) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (articleCollectionJpaRepository.existsByArticleIdAndUserId(articleId, userId)) {
            articleCollectionJpaRepository.deleteByArticleIdAndUserId(articleId, userId);
            articleJpaRepository.deleteCollectionsByArticleId(articleId);
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.ARTICLE_NOT_COLLECTED, "该文章未被收藏！");
        }
    }

    /**
     * @param articleId 文章ID
     * @param userId    用户ID
     * @return 是否删除成功
     * @checked true
     */
    @Override
    public String articleDel(String articleId, String userId) {
        response = new Response();
        if (articleJpaRepository.existsByArticleId(articleId)) {
            //存在文章
            ArrayList<String> commentVoteList = new ArrayList<>();
            List<Reply> replies = replyJpaRepository.findAllByTargetId(articleId);
            List<Comment> comments = commentJpaRepository.findAllByTargetId(articleId);
            for (Reply reply : replies) {
                commentVoteList.add(reply.getReplyId());
            }
            for (Comment comment : comments) {
                commentVoteList.add(comment.getCommentId());
            }
            commentVoteJpaRepository.deleteAllByTargetId(commentVoteList);//删除所有关于该文章评论或回复的支持反对操作
            replyJpaRepository.deleteByTargetId(articleId);//删除回复
            commentJpaRepository.deleteByTargetId(articleId);//删除评论
            articleCollectionJpaRepository.deleteByArticleId(articleId);//删除文章收藏
            articleJpaRepository.deleteByArticleId(articleId);//删除文章
            userJpaRepository.deleteArticlesByUserId(1, userId);//将用户的文章数-1
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.ARTICLE_NOT_EXIST, "文章不存在！");
        }
    }

    /**
     * @param articleId
     * @param title
     * @param text
     * @param articleSum
     * @param typeId
     * @param thumbnail
     * @param statusId
     * @Description 更新文章
    */
    @Override
    public String articleUpdate(String articleId, String title, String text, String articleSum, String typeId, String thumbnail, int statusId) {
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        response = new Response();
        long updateTime = System.currentTimeMillis();

        articleJpaRepository.updateByArticleId(title, text, articleSum, typeId, thumbnail, statusId, updateTime, articleId);

        return response.sendSuccess();

    }

    @Override
    public String draftList(int pageNum) {
        return null;
    }

    /**
     * @param article_id
     * @Description 访问文章
    */
    @Override
    public String visitArticle(String article_id) {
        response = new Response();
        articleJpaRepository.updateViewsByArticleId(article_id);
        return response.sendSuccess();
    }
}
