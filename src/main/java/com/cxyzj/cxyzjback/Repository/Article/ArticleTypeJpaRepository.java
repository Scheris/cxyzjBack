package com.cxyzj.cxyzjback.Repository.Article;

import com.cxyzj.cxyzjback.Bean.Article.ArticleType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: 夏
 * @DATE: 2018/9/17 16:19
 * @Description:
 */
public interface ArticleTypeJpaRepository extends JpaRepository<ArticleType, Integer> {

    ArticleType findByTypeId(String typeId);

}
