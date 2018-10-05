package com.cxyzj.cxyzjback.Repository.Article;

import com.cxyzj.cxyzjback.Bean.Article.Article;
import com.cxyzj.cxyzjback.Bean.User.Attention;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

/**
 * @Auther: 夏
 * @DATE: 2018/9/12 10:08
 * @Description:
 */
public interface ArticleJpaRepository extends JpaRepository<Article, Integer> {


    Article findByArticleId(String articleId);

    List<Article> findByArticleId(Collection<String> articleId);

    Page<Article> findAllByUserId(Pageable pageable, String userId);

    @Transactional
    @Modifying
    @Query(value = "update article set comments=?1 where article_id=?2", nativeQuery = true)
    void updateCommentsByArticleId(int comments, String articleId);

    @Transactional
    @Modifying
    @Query(value = "update article set levels=?1 where article_id=?2", nativeQuery = true)
    void updateLevelsByArticleId(int levels, String articleId);

    @Transactional
    @Modifying
    @Query(value = "update article a set a.views=a.views+1 where article_id=?1", nativeQuery = true)
    void updateViewsByArticleId(String articleId);


    @Transactional
    @Modifying
    @Query(value = "update article SET title=?1, text=?2,article_sum=?3,label_id=?4,thumbnail=?5,status_id=?6, update_time=?7 WHERE article_id=?8", nativeQuery = true)
    void updateByArticleId(String title, String text, String articleSum, String labelId, String thumbnail, int statusId, long updateTime, String articleId);


    @Transactional
    @Query(value = "select comments from article where article_id=?1", nativeQuery = true)
    int findCommentsByArticleId(String articleId);

    @Transactional
    @Query(value = "select views from article where article_id=?1", nativeQuery = true)
    int findViewsByArticleId(String articleId);

    @Transactional
    @Query(value = "select levels from article where article_id=?1", nativeQuery = true)
    int findLevelsByArticleId(String articleId);


    boolean existsByArticleId(String articleId);


    @Transactional
    @Modifying
    @Query(value = "update article a set a.collections=a.collections+1 where article_id=?1", nativeQuery = true)
    void increaseCollectionsByArticleId(String articleId);

    @Transactional
    @Modifying
    @Query(value = "update article a set a.collections=a.collections-1 where article_id=?1", nativeQuery = true)
    void deleteCollectionsByArticleId(String articleId);

    @Transactional
    @Modifying
    @Query(value = "delete from article where article_id=?1", nativeQuery = true)
    void deleteByArticleId(String articleId);
}
