package com.cxyzj.cxyzjback.Data.Article;

import com.cxyzj.cxyzjback.Bean.Article.ArticleLabel;
import lombok.Data;

/**
 * @Auther: 夏
 * @DATE: 2018/9/17 16:39
 * @Description:
 */

@Data
public class ArticleLabelBasic implements com.cxyzj.cxyzjback.Data.Data {

    private String label_id;

    private String label_name;

    public ArticleLabelBasic(ArticleLabel articleLabel){
        this.label_id = articleLabel.getLabelId();
        this.label_name = articleLabel.getLabelName();
    }

    @Override
    public String getName() {
        return "label";
    }
}
