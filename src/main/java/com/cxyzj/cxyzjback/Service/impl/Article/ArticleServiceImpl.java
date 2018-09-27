package com.cxyzj.cxyzjback.Service.impl.Article;

import com.cxyzj.cxyzjback.Bean.Article.Article;
import com.cxyzj.cxyzjback.Bean.Article.ArticleCollection;
import com.cxyzj.cxyzjback.Bean.Article.ArticleType;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.Article.ArticleBasic;
import com.cxyzj.cxyzjback.Data.Article.ArticleTypeBasic;
import com.cxyzj.cxyzjback.Data.User.UserBasic;
import com.cxyzj.cxyzjback.Repository.Article.*;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Article.ArticleService;
import com.cxyzj.cxyzjback.Utils.Response;
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

    private Response response;
    private Article article;
    private String userId;

    @Override
    public String writeArticle(String title, String text, String type_id, String article_sum,
                               String thumbnail, int status_id, String user_id) {
        response = new Response();
        article = new Article();
        article.setTitle(title);
        article.setText(text);
        article.setTypeId(type_id);
        article.setArticleSum(article_sum);
        article.setThumbnail(thumbnail);
        article.setStatus_id(status_id);
        article.setUserId(user_id);
        article.setUpdateTime(System.currentTimeMillis());

        article = articleJpaRepository.save(article);

        response.insert("article_id", article.getArticleId());
        return response.sendSuccess();
    }

    @Override
    public String getTypes() {
        response = new Response();
        ArticleType[] articleTypes = articleTypeJpaRepository.findAll().toArray(new ArticleType[0]);
        List<ArticleTypeBasic> articleTypeList = new ArrayList<>();
        for (int i = 0; i < articleTypes.length; i++) {
            ArticleTypeBasic articleTypeBasic = new ArticleTypeBasic(articleTypes[i]);
            articleTypeList.add(articleTypeBasic);
        }
        response.insert("type", articleTypeList);
        return response.sendSuccess();
    }

    @Override
    public String articleDetails(String articleId) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Article article = articleJpaRepository.findByArticleId(articleId);

        ArticleBasic articleBasic = new ArticleBasic(article);
        User user = userJpaRepository.findByUserId(article.getUserId());
        ArticleType articleType = articleTypeJpaRepository.findByTypeId(article.getTypeId());

        if (userId.equals(article.getUserId())) {
            articleBasic.set_author(true);
            articleBasic.setAllow_delete(true);
            articleBasic.setAllow_edit(true);
            articleBasic.set_collected(false);
            response.insert("article", articleBasic);

        } else {
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

    @Override
    public String collect(String articleId) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!articleCollectionJpaRepository.existsByArticleIdAndUserId(articleId, userId)) {
            ArticleCollection articleCollection = new ArticleCollection();
            articleCollection.setArticleId(articleId);
            articleCollection.setUserId(userId);

            articleCollectionJpaRepository.save(articleCollection);
            int collections = articleJpaRepository.findCollectionsByArticleId(articleId) + 1;
            articleJpaRepository.updateCollectionsByArticleId(collections, articleId);
        } else {
            this.collectDel(articleId);
        }

        return response.sendSuccess();
    }

    @Override
    public String collectDel(String articleId) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (articleCollectionJpaRepository.existsByArticleIdAndUserId(articleId, userId)) {
            articleCollectionJpaRepository.deleteByArticleIdAndUserId(articleId, userId);

            int collections = articleJpaRepository.findCollectionsByArticleId(articleId) - 1;
            articleJpaRepository.updateCollectionsByArticleId(collections, articleId);
        } else {
            this.collect(articleId);
        }

        return response.sendSuccess();

    }

    @Override
    public String articleDel(String articleId, String userId) {
        response = new Response();

        if (articleJpaRepository.existsByArticleId(articleId)) {
            replyJpaRepository.deleteByTargetId(articleId);
            commentJpaRepository.deleteByTargetId(articleId);
            articleCollectionJpaRepository.deleteByArticleId(articleId);

            articleJpaRepository.deleteByArticleId(articleId);

            return response.sendSuccess();

        } else {
            return response.sendFailure(128, "文章已删除或者您没有权限！");
        }
    }
}
