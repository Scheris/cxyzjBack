package com.cxyzj.cxyzjback.Service.impl.User.front;


import com.cxyzj.cxyzjback.Bean.Article.Article;
import com.cxyzj.cxyzjback.Bean.Article.ArticleLabel;
import com.cxyzj.cxyzjback.Bean.User.Attention;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.Article.ArticleBasic;
import com.cxyzj.cxyzjback.Data.Article.ArticleLabelBasic;
import com.cxyzj.cxyzjback.Data.Article.ArticleList;
import com.cxyzj.cxyzjback.Data.Other.PageData;
import com.cxyzj.cxyzjback.Data.User.OtherDetails;
import com.cxyzj.cxyzjback.Repository.Article.ArticleJpaRepository;
import com.cxyzj.cxyzjback.Repository.Article.ArticleLabelJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserAttentionJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.User.front.UserListGetService;
import com.cxyzj.cxyzjback.Utils.Constant;
import com.cxyzj.cxyzjback.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author 夏
 * @Date 15:50 2018/8/30
 * @Description: 用户列表信息获取
 * @checked false
 */

@Service
public class UserListGetServiceImpl implements UserListGetService {

    private final UserJpaRepository userJpaRepository;

    private final UserAttentionJpaRepository userAttentionJpaRepository;

    private final ArticleLabelJpaRepository articleLabelJpaRepository;

    private final ArticleJpaRepository articleJpaRepository;
    private Response response;

    @Autowired
    public UserListGetServiceImpl(UserJpaRepository userJpaRepository, UserAttentionJpaRepository userAttentionJpaRepository, ArticleLabelJpaRepository articleLabelJpaRepository, ArticleJpaRepository articleJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.userAttentionJpaRepository = userAttentionJpaRepository;
        this.articleLabelJpaRepository = articleLabelJpaRepository;
        this.articleJpaRepository = articleJpaRepository;
    }

    /**
     * @param userId  用户ID
     * @param pageNum 页码（从0开始）
     * @return 关注列表
     * @checked true
     */
    @Override
    public String getAttentionList(String userId, int pageNum) {
        response = new Response();
        Page<Attention> attentionPage = pageSelect(pageNum, Constant.FOCUS, userId);
        PageData pageData = new PageData(attentionPage, pageNum);
        ArrayList<OtherDetails> otherDetailsArrayList = getAttentionsOrFans(attentionPage.iterator(), true);
        response.insert(pageData);
        response.insert("attentions", otherDetailsArrayList);
        return response.sendSuccess();
    }

    /**
     * @param userId  用户ID
     * @param pageNum 页码（从0开始）
     * @return 粉丝列表
     * @checked true
     */
    @Override
    public String getFansList(String userId, int pageNum) {
        response = new Response();
        Page<Attention> attentionPage = pageSelect(pageNum, Constant.FOLLOWED, userId);
        PageData pageData = new PageData(attentionPage, pageNum);
        ArrayList<OtherDetails> otherDetailsArrayList = getAttentionsOrFans(attentionPage.iterator(), false);
        response.insert(pageData);
        response.insert("fans", otherDetailsArrayList);
        return response.sendSuccess();
    }

    /**
     * 没写好，待定。。。
     */
    @Override
    public String getArticleList(String userId, int pageNum) {
        response = new Response();

        Page<Article> articlePage = articlePage(pageNum, userId);//获取到了一页的文章信息
        PageData pageData = new PageData(articlePage, pageNum);//读取页面信息数据
        response.insert("list", getArticleList(articlePage.iterator()));
        response.insert(pageData);
        return response.sendSuccess();
    }

    /**
     * 没写好，待定。。。
     */
    @Override
    public String userComments(String user_id, int pageNum) {
        return null;
    }

    private Page<Article> articlePage(int pageNum, String userId) {
        Sort sort = new Sort(Sort.DEFAULT_DIRECTION, "articleId");//排序方式，按照文章id进行默认排序（从小到大）
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_USER_ARTICLE, sort);//设置分页信息，参数为：页码数，一次获取的个数，排序方式
        return articleJpaRepository.findAllByUserId(pageable, userId);
    }

    /**
     * @param pageNum 指定的页数
     * @param status  指定状态
     * @param userId  指定要查询的用户id
     * @return 分页查询结果
     * @checked true
     */
    private Page<Attention> pageSelect(int pageNum, int status, String userId) {
        Sort sort = new Sort(Sort.DEFAULT_DIRECTION, "userId");
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_ATTENTION_USER, sort);
        return userAttentionJpaRepository.findAllByUserIdAndStatusOrStatus(pageable, userId, status, Constant.EACH);//互相关注也包含在内
    }

    /**
     * @param attentionIterator 要处理的用户的迭代器
     * @param isAttention       是否是关注
     * @return 处理后的用户信息
     * @checked true
     */
    private ArrayList<OtherDetails> getAttentionsOrFans(Iterator<Attention> attentionIterator, boolean isAttention) {
        ArrayList<String> userIdList = new ArrayList<>();
        while (attentionIterator.hasNext()) {
            userIdList.add(attentionIterator.next().getTargetUser());
        }
        ArrayList<OtherDetails> otherDetailsArrayList = new ArrayList<>();
        List<User> userList = userJpaRepository.findAllById(userIdList);
        for (User user : userList) {
            otherDetailsArrayList.add(new OtherDetails(user, isAttention));
        }
        return otherDetailsArrayList;
    }

    /**
     * @param articleIterator 用户文章列表迭代器
     * @return 用户文章列表
     */
    private ArrayList<ArticleList> getArticleList(Iterator<Article> articleIterator) {
        ArrayList<ArticleBasic> articles = new ArrayList<>();//文章列表
        ArrayList<String> labelIdList = new ArrayList<>();//文章id列表（用于查询label）
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        while (articleIterator.hasNext()) {
            Article article = articleIterator.next();//获取一篇文章
            ArticleBasic articleBasic = new ArticleBasic(article);//封装文章数据
            articleBasic.IsAuthor(userId.equals(article.getUserId()));//设置是否为作者
            articles.add(articleBasic);
            labelIdList.add(article.getLabelId());//读取文章标签id
        }

        ArrayList<ArticleLabel> labelList = new ArrayList<>();//获取标签列表
        for (String id : labelIdList) {
            labelList.add(articleLabelJpaRepository.findByLabelId(id));//查询每一个label信息
        }
        ArrayList<ArticleList> resultList = new ArrayList<>();//返回的列表信息
        for (int i = 0; i < articles.size(); i++) {
            ArticleList articleList = new ArticleList(articles.get(i), new ArticleLabelBasic(labelList.get(i)));//封装数据
            resultList.add(articleList);
        }
        return resultList;
    }
}
