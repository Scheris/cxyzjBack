package com.cxyzj.cxyzjback.Data.Article;

import com.cxyzj.cxyzjback.Data.User.OtherDetails;
import com.cxyzj.cxyzjback.Data.User.OtherSimple;
import lombok.Data;

/**
 * @Package com.cxyzj.cxyzjback.Data.Article
 * @Author Yaser
 * @Date 2018/10/05 19:50
 * @Description:
 */
@Data
public class ArticleList implements com.cxyzj.cxyzjback.Data.Data {


    private ArticleBasic article;
    private ArticleLabelBasic label;
    private OtherSimple user;

    public ArticleList(ArticleBasic article, ArticleLabelBasic label, OtherSimple user) {
        this.article = article;
        this.label = label;
        this.user = user;
    }

    public ArticleList(ArticleBasic article, ArticleLabelBasic label) {
        this.article = article;
        this.label = label;
    }

    @Override
    public String getName() {
        return "list";
    }
}
