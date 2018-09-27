package com.cxyzj.cxyzjback.Data.Article;

import com.cxyzj.cxyzjback.Bean.Article.ArticleType;
import lombok.Data;

/**
 * @Auther: 夏
 * @DATE: 2018/9/17 16:39
 * @Description:
 */

@Data
public class ArticleTypeBasic implements com.cxyzj.cxyzjback.Data.Data {

    private String type_id;

    private String type_name;

    public ArticleTypeBasic(ArticleType articleType){
        this.type_id = articleType.getTypeId();
        this.type_name = articleType.getTypeName();
    }

    @Override
    public String getName() {
        return "type";
    }
}
