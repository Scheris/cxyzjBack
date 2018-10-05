package com.cxyzj.cxyzjback.Repository.Article;

import com.cxyzj.cxyzjback.Bean.Article.ArticleLabel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: 夏
 * @DATE: 2018/9/17 16:19
 * @Description:
 */
public interface ArticleLabelJpaRepository extends JpaRepository<ArticleLabel, Integer> {

    ArticleLabel findByLabelId(String labelId);

}
