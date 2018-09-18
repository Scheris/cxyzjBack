package com.cxyzj.cxyzjback.Repository.Article;

import com.cxyzj.cxyzjback.Bean.Article.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Auther: 夏
 * @DATE: 2018/9/6 15:10
 * @Description:
 */
public interface CommentJpaRepository extends JpaRepository<Comment, Integer> {

    Comment findByCommentId(String comment_id);

    boolean existsByCommentId(String commentId);

    @Transactional
    @Modifying
    @Query(value = "delete from comment where comment_id=?1", nativeQuery = true)
    int deleteByCommentId(String commentId);

    @Transactional
    @Modifying
    @Query(value = "delete from comment where target_id=?1", nativeQuery = true)
    int deleteByTargetId(String targetId);

    @Transactional
    @Query(value = "select level from comment where target_id=?1", nativeQuery = true)
    List getLevel(String topic_id);

    @Transactional
    @Modifying
    @Query(value = "update comment set children=?1 where comment_id=?2", nativeQuery = true)
    int updateChildren(int children, String commentId);

    @Transactional
    @Query(value = "select children from comment where comment_id=?1", nativeQuery = true)
    int findChildren(String commentId);

    @Transactional
    @Query(value = "select * from comment where target_id=?1", nativeQuery = true)
    List<Comment> findListByTargetId(String article_id);

    @Transactional
    @Query(value = "select * from comment LIMIT ?1,?2", nativeQuery = true)
    List findAll(int startIndex, int i);

    @Transactional
    @Query(value = "select support from comment where comment_id=?1", nativeQuery = true)
    int findSupportByCommentId(String commentId);

    @Transactional
    @Modifying
    @Query(value = "update comment set support=?1 where comment_id=?2", nativeQuery = true)
    int updateCommentSupport(int support, String commentId);

    @Transactional
    @Query(value = "select object from comment where comment_id=?1", nativeQuery = true)
    int findObjectByCommentId(String commentId);

    @Transactional
    @Modifying
    @Query(value = "update comment set object=?1 where comment_id=?2", nativeQuery = true)
    int updateCommentObject(int object, String commentId);
}
