package com.cxyzj.cxyzjback.Repository.User;

import com.cxyzj.cxyzjback.Bean.User.Attention;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface UserAttentionJpaRepository extends JpaRepository<Attention, Integer> {


    Page<Attention> findAllByUserIdAndStatus(Pageable pageable, String userID, int status);

    boolean existsByUserIdAndTargetUser(String userId, String targetUser);

    void deleteByUserId(String userId);

    void deleteByUserIdAndTargetUser(String userId, String targetUser);


    @Query(value = "select status_id from attention where user_id=?1 and target_user=?2", nativeQuery = true)
    int findStatusByUserIdAndTargetUser(String userId, String targetUser);

    @Modifying
    @Query(value = "update attention p set p.status_id=?1 where p.user_id=?2 and p.target_user=?3", nativeQuery = true)
    void updateStatusByUserAndTargetUser(int status, String userId, String targetId);

}
