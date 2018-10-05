package com.cxyzj.cxyzjback.Data.Article;

import com.cxyzj.cxyzjback.Bean.Article.Article;
import lombok.Data;

/**
 * @Auther: 夏
 * @DATE: 2018/9/17 16:58
 * @Description:
 */

@Data
public class ArticleBasic implements com.cxyzj.cxyzjback.Data.Data {

    private String article_id;
    private String title;
    private String article_sum;
    private long update_time;
    private int views;
    private int comments;
    private int collections;
    private String thumbnail;
    private String text;
    private boolean is_collected;
    private boolean allow_delete;
    private boolean allow_edit;
    private int status_id;
    private boolean is_author;

    public ArticleBasic(Article article){
        this.article_id = article.getArticleId();
        this.title = article.getTitle();
        this.article_sum = article.getArticleSum();
        this.update_time = article.getUpdateTime();
        this.views = article.getViews();
        this.comments = article.getComments();
        this.collections = article.getCollections();
        this.thumbnail = article.getThumbnail();
        this.text = article.getText();
        this.is_collected = false;
        this.allow_delete = false;
        this.allow_edit = false;
        this.status_id = article.getStatusId();
        this.is_author = false;
    }

    @Override
    public String getName() {
        return "article";
    }
}
