package com.cxyzj.cxyzjback.Service.impl.Article;

import com.cxyzj.cxyzjback.Bean.Article.*;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.Article.ArticleBasic;
import com.cxyzj.cxyzjback.Data.Article.ArticleLabelBasic;
import com.cxyzj.cxyzjback.Data.Article.ArticleList;
import com.cxyzj.cxyzjback.Data.Other.PageData;
import com.cxyzj.cxyzjback.Data.User.OtherDetails;
import com.cxyzj.cxyzjback.Data.User.UserBasic;
import com.cxyzj.cxyzjback.Repository.Article.*;
import com.cxyzj.cxyzjback.Repository.User.UserAttentionJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Article.ArticleService;
import com.cxyzj.cxyzjback.Utils.Constant;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import com.cxyzj.cxyzjback.Utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
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
    private ArticleLabelJpaRepository articleLabelJpaRepository;

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

    @Autowired
    private UserAttentionJpaRepository userAttentionJpaRepository;

    private Response response;
    private String userId;


    /**
     * @param title       文章标题
     * @param text        文章内容
     * @param label_id    文章类型
     * @param article_sum 文章概要
     * @param thumbnail   缩略图
     * @param status_id   文章状态
     * @param user_id     用户id
     * @return 文章ID
     * @checked true
     */
    @Override
    public String writeArticle(String title, String text, String label_id, String article_sum,
                               String thumbnail, int status_id, String user_id) {
        response = new Response();
        Article article = new Article();
        article.setTitle(title);
        article.setText(text);
        article.setLabelId(label_id);
        article.setArticleSum(article_sum);
        article.setThumbnail(thumbnail);
        article.setStatusId(status_id);
        article.setUserId(user_id);
        article.setUpdateTime(System.currentTimeMillis());

        articleLabelJpaRepository.updateQuantityByLabelId(1, label_id);
        article = articleJpaRepository.save(article);
        userJpaRepository.increaseArticlesByUserId(1, user_id);//文章数+1
        response.insert("article_id", article.getArticleId());
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
        ArticleLabel articleLabel = articleLabelJpaRepository.findByLabelId(article.getLabelId());

        if (userId.equals(article.getUserId())) {
            //是作者
            articleBasic.set_author(true);
            articleBasic.setAllow_delete(true);
            articleBasic.setAllow_edit(true);
            articleBasic.set_collected(false);
            response.insert("user", new UserBasic(user));
        } else {
            //不是作者
            //如果已收藏该文章
            if (articleCollectionJpaRepository.existsByArticleIdAndUserId(articleId, userId)) {
                articleBasic.set_collected(true);
            }
            response.insert("user", new OtherDetails(user));
        }
        response.insert("article", articleBasic);
        response.insert("label", new ArticleLabelBasic(articleLabel));
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
        String labelId = articleJpaRepository.findLabelIdByArticleId(articleId).getLabelId();
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
            articleLabelJpaRepository.updateQuantityByLabelId(-1, labelId);
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.ARTICLE_NOT_EXIST, "文章不存在！");
        }
    }

    @Override
    public String articleUpdate(String articleId, String title, String text, String articleSum, String labelId, String thumbnail, int statusId) {
        response = new Response();
        long updateTime = System.currentTimeMillis();
        articleJpaRepository.updateByArticleId(title, text, articleSum, labelId, thumbnail, statusId, updateTime, articleId);

        return response.sendSuccess();
    }

    @Override
    public String draftList(int pageNum) {
        response = new Response();
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();//获取用户ID
        Page<Article> articlePage = articlePage(pageNum, Constant.DRAFT, userId);//获取到了一页的文章信息
        PageData pageData = new PageData(articlePage, pageNum);//读取页面信息数据
        response.insert("list", getArticleList(articlePage.iterator(), false));
        response.insert(pageData);
        return response.sendSuccess();
    }

    @Override
    public String visitArticle(String article_id) {
        response = new Response();
        articleJpaRepository.updateViewsByArticleId(article_id);
        return response.sendSuccess();
    }

    @Override
    public String getArticle(String labelId, int pageNum) {
        response = new Response();
        Page<Article> articlePage = allArticle(pageNum, Constant.PUBLISH);
        PageData pageData = new PageData(articlePage, pageNum);
        response.insert("list", getArticleList(articlePage.iterator(), true));
        response.insert(pageData);
        return response.sendSuccess();
    }

    /**
     * @param pageNum  页码（从0开始）
     * @param statusId 文章状态
     * @param userId   用户ID
     * @return 文章查询结果信息
     * @Description: 通过页码，文章状态和用户来查询指定的文章列表
     */
    private Page<Article> articlePage(int pageNum, int statusId, String userId) {
        Sort sort = new Sort(Sort.DEFAULT_DIRECTION, "articleId");//排序方式，按照文章id进行默认排序（从小到大）
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_ARTICLE, sort);//设置分页信息，参数为：页码数，一次获取的个数，排序方式
        return articleJpaRepository.findAllByUserIdAndStatusId(pageable, userId, statusId);
    }

    private Page<Article> allArticle(int pageNum, int statusId) {
        Sort sort = new Sort(Sort.DEFAULT_DIRECTION, "articleId");
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_ARTICLE, sort);
        return articleJpaRepository.findAllByStatusId(pageable, statusId);
    }

    /**
     * @param articleIterator 文章迭代器
     * @param needUser        是否需要用户
     * @return 文章列表信息
     */
    private List<ArticleList> getArticleList(Iterator<Article> articleIterator, boolean needUser) {
        ArrayList<ArticleBasic> articles = new ArrayList<>();//文章列表
        ArrayList<String> labelIdList = new ArrayList<>();//文章id列表（用于查询label）
        ArrayList<String> userIdList = new ArrayList<>();//用户id列表
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        while (articleIterator.hasNext()) {
            Article article = articleIterator.next();//获取一篇文章
            ArticleBasic articleBasic = new ArticleBasic(article);//封装文章数据
            articleBasic.IsAuthor(userId.equals(article.getUserId()));//设置是否为作者
            articles.add(articleBasic);
            labelIdList.add(article.getLabelId());//读取文章标签id
            if (needUser) {
                userIdList.add(article.getUserId());//读取文章作者
            }
        }

        ArrayList<ArticleLabel> labelList = new ArrayList<>();//获取标签列表
        for (String id : labelIdList) {
            labelList.add(articleLabelJpaRepository.findByLabelId(id));//查询每一个label信息
        }
        ArrayList<User> userList = new ArrayList<>();
        if (needUser) {
            for (String id : userIdList) {
                userList.add(userJpaRepository.findByUserId(id));//查询每一个user信息
            }
        }
        ArrayList<ArticleList> resultList = new ArrayList<>();//返回的列表信息
        for (int i = 0; i < articles.size(); i++) {
            ArticleList articleList;
            if (needUser) {
                User user = userList.get(i);//获取文章用户
                boolean status = false;
                if (userAttentionJpaRepository.existsByUserIdAndTargetUser(userId, user.getUserId())) {
                    status = userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, user.getUserId()) == Constant.FOCUS ||
                            userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, user.getUserId()) == Constant.EACH;
                }
                OtherDetails otherDetails = new OtherDetails(user, status);//封装用户数据
                articleList = new ArticleList(articles.get(i), new ArticleLabelBasic(labelList.get(i)), otherDetails);//封装数据
            } else {
                articleList = new ArticleList(articles.get(i), new ArticleLabelBasic(labelList.get(i)));//封装数据
            }
            resultList.add(articleList);
        }
        return resultList;
    }

}
