package com.cxyzj.cxyzjback.Bean.Article;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Auther: 夏
 * @DATE: 2018/9/6 14:59
 * @Description:
 */

@Entity
@Data
@Table(name = "comment_vote")
public class CommentVote {

        @Id
        @GeneratedValue(generator = "SnowflakeIdGenerator")//自定义ID生成器
        @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "com.cxyzj.cxyzjback.Utils.SnowflakeIdGenerator")
        @Column(name = "comment_vote_id")
        private String commentVoteId;

        @Column(name = "user_id")
        private String userId;

        @Column(name = "target_id")
        private String targetId;

        @Column(name = "status_id")
        private int status;

}
