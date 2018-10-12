package com.cxyzj.cxyzjback.Repository.Article;

import com.cxyzj.cxyzjback.Bean.Article.ArticleLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

/**
 * @Auther: 夏
 * @DATE: 2018/9/17 16:19
 * @Description:
 */
public interface ArticleLabelJpaRepository extends JpaRepository<ArticleLabel, String> {

    ArticleLabel findByLabelId(String labelId);

    @Transactional
    @Modifying
    @Query(value = "update article_label a set a.quantity=a.quantity+?1 where label_id=?2", nativeQuery = true)
    void updateQuantityByLabelId(int n, String label_id);
}
