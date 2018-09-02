package com.cxyzj.cxyzjback.Repository.User;

import com.cxyzj.cxyzjback.Bean.User.Attention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserAttentionJpaRepository extends JpaRepository<Attention, Integer> {

    List<Attention> findByUserId(String userId);

    @Transactional
    @Query(value = "select * from attention where user_id=?1 and target_user=?2",nativeQuery = true)
    Attention findByUserIdAndTargetUser(String userId, String otherId);

    @Transactional
    @Query(value = "select status_id from attention where user_id=?1 and target_user=?2",nativeQuery = true)
    int findStatusByUserIdAndTargetUser(String userId ,String targetUser);

    @Transactional
    @Query(value = "select * from attention LIMIT ?1,?2", nativeQuery = true)
    List findAll(int startIndex, int i);

    @Transactional
    @Query(value = "select * from attention where user_id=?3 LIMIT ?1,?2", nativeQuery = true)
    Attention findAttention(int startIndex, int i, String userId);

    @Transactional
    @Modifying
    @Query(value = "update attention p set p.status_id=?1 where p.target_user=?2", nativeQuery = true)
    void updateStatusByTargetUser(int status, String targetId);

    @Transactional
    @Modifying
    @Query(value = "insert into attention(user_id,target_user,status_id) values(?1,?2,?3)", nativeQuery = true)
    int createAttention(String userId, String targetUser, int status);

    @Transactional
    void deleteByUserId(String userId);



//    String findAttentionBy

}
