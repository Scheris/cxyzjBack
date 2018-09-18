package com.cxyzj.cxyzjback.Service.Interface.Article;

/**
 * @Auther: 夏
 * @DATE: 2018/9/12 10:10
 * @Description:
 */
public interface ArticleService {
    String writeArticle(String title, String text, String typeId, String articleSum, String thumbnail, int statusId, String userId);

    String getTypes();

    String articleDetails(String articleId);

    String collect(String articleId);

    String collectDel(String articleId);

    String articleDel(String articleId, String userId);
}
