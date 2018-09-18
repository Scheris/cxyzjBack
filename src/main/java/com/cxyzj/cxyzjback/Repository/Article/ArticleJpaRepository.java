package com.cxyzj.cxyzjback.Repository.Article;

import com.cxyzj.cxyzjback.Bean.Article.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @Auther: 夏
 * @DATE: 2018/9/12 10:08
 * @Description:
 */
public interface ArticleJpaRepository extends JpaRepository<Article, Integer> {

    Article findByArticleId(String articleId);

    Article findByUpdateTime(long updateTime);

    @Transactional
    @Modifying
    @Query(value = "update article set comments=?1 where article_id=?2", nativeQuery = true)
    int updateCommentsByArticleId(int comments, String articleId);

    @Transactional
    @Modifying
    @Query(value = "update article set levels=?1 where article_id=?2", nativeQuery = true)
    int updateLevelsByArticleId(int levels, String articleId);

    @Transactional
    @Query(value = "select comments from article where article_id=?1", nativeQuery = true)
    int findCommentsByArticleId(String articleId);

    @Transactional
    @Query(value = "select levels from article where article_id=?1", nativeQuery = true)
    int findLevelsByArticleId(String articleId);


    boolean existsByArticleId(String articleId);

    @Transactional
    @Query(value = "select collections from article where article_id=?1", nativeQuery = true)
    int findCollectionsByArticleId(String articleId);

    @Transactional
    @Modifying
    @Query(value = "update article set collections=?1 where article_id=?2", nativeQuery = true)
    int updateCollectionsByArticleId(int collections, String articleId);

    @Transactional
    @Modifying
    @Query(value = "delete from article where article_id=?1", nativeQuery = true)
    int deleteByArticleId(String articleId);
}
