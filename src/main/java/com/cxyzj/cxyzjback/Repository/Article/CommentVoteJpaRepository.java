package com.cxyzj.cxyzjback.Repository.Article;

import com.cxyzj.cxyzjback.Bean.Article.CommentVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

/**
 * @Auther: 夏
 * @DATE: 2018/9/6 15:14
 * @Description:
 */
public interface CommentVoteJpaRepository extends JpaRepository<CommentVote, Integer> {

    @Transactional
    @Query(value = "select * from comment_vote where user_id=?1 and target_id=?2", nativeQuery = true)
    CommentVote findByUserIdAndTargetId(String userId, String targetId);

    @Transactional
    @Modifying
    @Query(value = "delete from comment_vote where target_id=?1", nativeQuery = true)
    void deleteByTargetId(String target_id);

    @Transactional
    @Modifying
    void deleteAllByTargetId(Collection<String> targetId);

    boolean existsByUserIdAndTargetId(String userId, String targetId);
}
