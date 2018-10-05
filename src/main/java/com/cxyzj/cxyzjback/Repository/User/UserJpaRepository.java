package com.cxyzj.cxyzjback.Repository.User;

import com.cxyzj.cxyzjback.Bean.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Package com.cxyzj.cxyzjback.Repository
 * @Author Yaser
 * @Date 2018/07/29 15:12
 * @Description:
 */
public interface UserJpaRepository extends JpaRepository<User, String> {
    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByEmailOrPhoneOrNickname(String email, String phone, String nickname);

    boolean existsByUserId(String UserId);

    boolean existsByNickname(String nickname);


    User findByUserId(String userId);


    User findByEmail(String email);

    User findByPhone(String phone);

    @Transactional
    @Modifying
    @Query(value = "update user p set p.password=?1 where p.phone=?2", nativeQuery = true)
    void updatePasswordByPhone(String password, String phone);


    @Transactional
    @Modifying
    @Query(value = "update user p set p.password=?1 where p.email=?2", nativeQuery = true)
    void updatePasswordByEmail(String password, String email);

    @Transactional
    @Modifying
    @Query(value = "update user p set p.nickname=?1 where p.user_id=?2", nativeQuery = true)
    void updateNicknameByUserId(String nickname, String userId);

    @Transactional
    @Modifying
    @Query(value = "update user p set p.head_url=?1 where p.user_id=?2", nativeQuery = true)
    void updateHeadByUserId(String headUrl, String userId);


    @Transactional
    @Modifying
    @Query(value = "update user p set p.gender=?1 where p.user_id=?2", nativeQuery = true)
    void updateGenderByUserId(String gender, String userId);

    @Transactional
    @Modifying
    @Query(value = "update user p set p.introduce=?1 where p.user_id=?2", nativeQuery = true)
    void updateIntroduceByUserId(String introduce, String userId);

    @Transactional
    @Modifying
    @Query(value = "update user p set p.theme_color=?1 where p.user_id=?2", nativeQuery = true)
    void updateThemeColorByUserId(String themeColor, String userId);

    @Transactional
    @Modifying
    @Query(value = "update user p set p.bg_url=?1 where p.user_id=?2", nativeQuery = true)
    void updateBgUrlByUserId(String bgUrl, String userId);

    @Transactional
    @Modifying
    @Query(value = "update user p set p.password=?1 where p.user_id=?2", nativeQuery = true)
    void updatePasswordByUserId(String password, String user_id);

    @Transactional
    @Modifying
    @Query(value = "update user p set p.phone=?1 where p.user_id=?2", nativeQuery = true)
    void updatePhoneByUserId(String phone, String user_id);

    @Transactional
    @Modifying
    @Query(value = "update user p set p.email=?1 where p.user_id=?2", nativeQuery = true)
    void updateEmailByUserId(String email, String user_id);

    @Transactional
    @Query(value = "select fans from user where user_id=?1", nativeQuery = true)
    int getUserFans(String userId);

    @Transactional
    @Modifying
    @Query(value = "update user p set p.fans=?1 where user_id=?2", nativeQuery = true)
    void updateFansByUserId(int fans, String userId);

    @Transactional
    @Query(value = "select attentions from user where user_id=?1", nativeQuery = true)
    int getUserAttentions(String userId);

    @Transactional
    @Modifying
    @Query(value = "update user p set p.attentions=?1 where user_id=?2", nativeQuery = true)
    void updateAttentionsByUserId(int attentions, String userId);

    @Transactional
    @Modifying
    @Query(value = "update user p set p.login_date=?1 where p.user_id=?2", nativeQuery = true)
    void updateLoginDateByUserId(long login_date, String userId);


    @Transactional
    @Modifying
    @Query(value = "update user set user.articles=user.articles+?1 where user.user_id=?2", nativeQuery = true)
    void increaseArticlesByUserId(int n, String userId);

    @Transactional
    @Modifying
    @Query(value = "update user set user.articles=user.articles-?1 where user.user_id=?2", nativeQuery = true)
    void deleteArticlesByUserId(int n, String userId);
}
