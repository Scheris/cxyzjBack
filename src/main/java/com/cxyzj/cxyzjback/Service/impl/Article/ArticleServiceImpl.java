package com.cxyzj.cxyzjback.Service.impl.Article;

import com.cxyzj.cxyzjback.Bean.Article.*;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.Article.ArticleBasic;
import com.cxyzj.cxyzjback.Data.Article.ArticleLabelBasic;
import com.cxyzj.cxyzjback.Data.Article.ArticleList;
import com.cxyzj.cxyzjback.Data.Other.PageData;
import com.cxyzj.cxyzjback.Data.User.OtherDetails;
import com.cxyzj.cxyzjback.Data.User.OtherSimple;
import com.cxyzj.cxyzjback.Data.User.UserBasic;
import com.cxyzj.cxyzjback.Repository.Article.*;
import com.cxyzj.cxyzjback.Repository.User.UserAttentionJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Article.ArticleService;
import com.cxyzj.cxyzjback.Utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @Auther: 夏
 * @DATE: 2018/9/12 10:10
 * @Description: 文章操作与文章信息获取的API（已检查过）
 * @checked true
 */
@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private final ArticleJpaRepository articleJpaRepository;

    private final ArticleLabelJpaRepository articleLabelJpaRepository;

    private final UserJpaRepository userJpaRepository;

    private final ArticleCollectionJpaRepository articleCollectionJpaRepository;

    private final ReplyJpaRepository replyJpaRepository;

    private final CommentJpaRepository commentJpaRepository;

    private final CommentVoteJpaRepository commentVoteJpaRepository;

    private final UserAttentionJpaRepository userAttentionJpaRepository;

    private Response response;
    private String userId;

    @Autowired
    public ArticleServiceImpl(ArticleJpaRepository articleJpaRepository, ArticleLabelJpaRepository articleLabelJpaRepository, UserJpaRepository userJpaRepository, ArticleCollectionJpaRepository articleCollectionJpaRepository, ReplyJpaRepository replyJpaRepository, CommentJpaRepository commentJpaRepository, CommentVoteJpaRepository commentVoteJpaRepository, UserAttentionJpaRepository userAttentionJpaRepository) {
        this.articleJpaRepository = articleJpaRepository;
        this.articleLabelJpaRepository = articleLabelJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.articleCollectionJpaRepository = articleCollectionJpaRepository;
        this.replyJpaRepository = replyJpaRepository;
        this.commentJpaRepository = commentJpaRepository;
        this.commentVoteJpaRepository = commentVoteJpaRepository;
        this.userAttentionJpaRepository = userAttentionJpaRepository;
    }


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

        articleLabelJpaRepository.updateQuantityByLabelId(1, label_id);//label下的文章数+1
        article = articleJpaRepository.save(article);
        userJpaRepository.increaseArticlesByUserId(1, user_id);//文章数+1
        response.insert("article_id", article.getArticleId());
        return response.sendSuccess();
    }


    /**
     * @param articleId 文章ID
     * @return 文章数据
     * @checked true
     * @Description: 访问文章获取文章详细数据
     * 以后可进行优化点：对于每一篇未缓存处理的文章，在初次访问可以做一个有时间限制(例如3分钟)的缓存，
     * 之后，每有一个人访问，这个时间就增加一点（例如增加1分钟），
     * 如果没有人访问则时间到了之后缓存自动删除
     */
    @Override
    public String articleDetails(String articleId) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();//获取访问用户id
        articleJpaRepository.updateViewsByArticleId(articleId);//更新文章访问次数
        Article article = articleJpaRepository.findByArticleId(articleId);//获取文章
        ArticleBasic articleBasic = new ArticleBasic(article);
        User articleUser = userJpaRepository.findByUserId(article.getUserId());//获取文章作者信息
        ArticleLabel articleLabel = articleLabelJpaRepository.findByLabelId(article.getLabelId());//获取文章标签信息

        if (userId.equals(article.getUserId())) {
            //是作者
            articleBasic.IsAuthor(true);
            response.insert(new UserBasic(articleUser));
        } else {
            //不是作者
            articleBasic.IsAuthor(false);
            if (articleCollectionJpaRepository.existsByArticleIdAndUserId(articleId, userId)) {//判断用户是否收藏了该文章
                articleBasic.set_collected(true);
            }
            boolean status = false;
            if (userAttentionJpaRepository.existsByUserIdAndTargetUser(userId, articleUser.getUserId())) {//判断是否关注
                status = userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, articleUser.getUserId()) == Constant.FOCUS ||
                        userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, articleUser.getUserId()) == Constant.EACH;
            }
            response.insert(new OtherDetails(articleUser, status));
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
            //未收藏
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
            articleJpaRepository.reduceCollectionsByArticleId(articleId);
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
            String labelId = articleJpaRepository.findLabelIdByArticleId(articleId).getLabelId();
            ArrayList<String> targetIds = new ArrayList<>();//评论操作的targetIds列表
            List<Reply> replies = replyJpaRepository.findAllByTargetId(articleId);//获取回复列表
            List<Comment> comments = commentJpaRepository.findByTargetId(articleId);//获取评论列表
            for (Reply reply : replies) {
                targetIds.add(reply.getReplyId());
            }
            for (Comment comment : comments) {
                targetIds.add(comment.getCommentId());
            }
            List<CommentVote> commentVoteList = commentVoteJpaRepository.findAllByTargetId(targetIds);//找到所有targetId对应的评论操作
            commentVoteJpaRepository.deleteInBatch(commentVoteList);//删除所有关于该文章评论或回复的支持反对操作
            replyJpaRepository.deleteByTargetId(articleId);//删除文章所有的回复
            commentJpaRepository.deleteByTargetId(articleId);//删除文章所有的评论
            //评论与回复的删除顺序需要注意，因为回复表中有评论的外键
            articleCollectionJpaRepository.deleteByArticleId(articleId);//删除文章收藏
            articleJpaRepository.deleteByArticleId(articleId);//删除文章
            userJpaRepository.deleteArticlesByUserId(1, userId);//将用户的文章数-1
            articleLabelJpaRepository.updateQuantityByLabelId(-1, labelId);//将对应标签下的文章数-1
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.ARTICLE_NOT_EXIST, "文章不存在！");
        }
    }

    /**
     * @param articleId  文章id
     * @param title      文章标题
     * @param text       文章内容
     * @param articleSum 文章概要
     * @param labelId    标签id
     * @param thumbnail  缩略图
     * @param statusId   文章状态id
     * @return 是否更新成功
     */
    @Override
    public String articleUpdate(String articleId, String title, String text, String articleSum, String labelId, String thumbnail, int statusId) {
        response = new Response();
        if (articleJpaRepository.existsByArticleId(articleId)) {
            long updateTime = System.currentTimeMillis();
            articleJpaRepository.updateByArticleId(title, text, articleSum, labelId, thumbnail, statusId, updateTime, articleId);
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.ARTICLE_NOT_EXIST, "文章不存在");
        }

    }

    @Override
    public String draftList(int pageNum) {
        response = new Response();
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();//获取用户ID
        Page<Article> articlePage = articlePage(pageNum, Constant.DRAFT, userId, Constant.NONE);//获取到了一页的文章信息
        PageData pageData = new PageData(articlePage, pageNum);//读取页面信息数据
        response.insert("list", getArticleList(articlePage.iterator(), false));
        response.insert(pageData);
        return response.sendSuccess();
    }

    @Override
    public String getArticleList(String labelId, int pageNum) {
        response = new Response();
        Page<Article> articlePage = articlePage(pageNum, Constant.PUBLISH, Constant.NONE, labelId);
        PageData pageData = new PageData(articlePage, pageNum);
        response.insert("list", getArticleList(articlePage.iterator(), true));
        response.insert(pageData);
        return response.sendSuccess();
    }

    /**
     * @param pageNum  页码（从0开始）
     * @param statusId 文章状态
     * @param userId   用户ID
     * @param labelId  文章标签ID
     * @return 文章查询结果信息
     * @checked true
     * @Description: 查询指定的文章列表，其中页码与文章状态为必选信息，其它为可选项，但userId与labelId不会同时存在
     */
    private Page<Article> articlePage(int pageNum, int statusId, String userId, String labelId) {
        Sort sort = new Sort(Sort.DEFAULT_DIRECTION, "articleId");//排序方式，按照文章id进行默认排序（从小到大）
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_ARTICLE, sort);//设置分页信息，参数为：页码数，一次获取的个数，排序方式
        if (!labelId.equals(Constant.NONE)) {
            //labelId存在
            return articleJpaRepository.findAllByLabelIdAndStatusId(pageable, labelId, statusId);
        } else {
            //labelId不存在
            if (!userId.equals(Constant.NONE)) {
                //userId存在
                return articleJpaRepository.findAllByUserIdAndStatusId(pageable, userId, statusId);
            } else {
                //都不存在
                return articleJpaRepository.findAllByStatusId(pageable, statusId);
            }
        }

    }

    /**
     * @param articleIterator 文章迭代器
     * @param needUser        是否需要用户
     * @return 文章列表信息
     * @checked true
     */
    private List<ArticleList> getArticleList(Iterator<Article> articleIterator, boolean needUser) {
        ArrayList<Article> articles = new ArrayList<>();//文章列表
        ArrayList<ArticleBasic> articleBasics = new ArrayList<>();//返回给前端的文章列表
        ArrayList<String> labelIdList = new ArrayList<>();//文章id列表（用于查询label）
        ArrayList<String> userIdList = new ArrayList<>();//用户id列表（用于查询user）
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        while (articleIterator.hasNext()) {//读取文章数据
            Article article = articleIterator.next();//获取一篇文章
            articles.add(article);
            ArticleBasic articleBasic = new ArticleBasic(article);//封装文章数据
            articleBasic.IsAuthor(userId.equals(article.getUserId()));//设置是否为作者
            articleBasics.add(articleBasic);
            labelIdList.add(article.getLabelId());//读取文章标签id
            if (needUser) {
                userIdList.add(article.getUserId());//读取文章作者
            }
        }
        //因为jpa在批量查询数据的时候，对于id重复的数据会自动合并，所以需要做一个映射处理
        List<ArticleLabel> labelList = articleLabelJpaRepository.findAllById(labelIdList);//获取标签列表
        ListToMap<ArticleLabel> labelListToMap = new ListToMap<>();
        HashMap<String, ArticleLabel> articleLabelMap = labelListToMap.getMap(labelList, "labelId", ArticleLabel.class);//标签映射map
        List<User> userList;
        HashMap<String, User> userMap = null;//用户映射map
        if (needUser) {
            userList = userJpaRepository.findAllById(userIdList);//获取用户列表
            ListToMap<User> userListToMap = new ListToMap<>();
            userMap = userListToMap.getMap(userList, "userId", User.class);//用户映射map
        }
        ArrayList<ArticleList> resultList = new ArrayList<>();//返回的列表信息
        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            ArticleList articleList;
            ArticleLabelBasic articleLabelBasic = new ArticleLabelBasic(articleLabelMap.get(article.getLabelId()));
            if (needUser) {
                User user = userMap.get(article.getUserId());//获取文章用户
                OtherSimple otherSimple = new OtherSimple(user);//封装用户数据，
                articleList = new ArticleList(articleBasics.get(i), articleLabelBasic, otherSimple);//封装数据
            } else {
                articleList = new ArticleList(articleBasics.get(i), articleLabelBasic);//封装数据
            }
            resultList.add(articleList);
        }
        return resultList;
    }

}
