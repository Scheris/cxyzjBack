package com.cxyzj.cxyzjback.Service.Interface.Article;

/**
 * @Auther: 夏
 * @DATE: 2018/9/12 10:10
 * @Description:
 */
public interface ArticleService {
    String writeArticle(String title, String text, String label_id, String articleSum, String thumbnail, int statusId, String userId);

    String articleDetails(String articleId);

    String collect(String articleId);

    String collectDel(String articleId);

    String articleDel(String articleId, String userId);

    String articleUpdate(String articleId, String title, String text, String articleSum, String labelId, String thumbnail, int statusId);

    String draftList(int pageNum);

    String visitArticle(String article_id);
}
