package com.cxyzj.cxyzjback.Bean.Article;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Auther: 夏
 * @DATE: 2018/9/6 15:00
 * @Description:
 */
@Entity
@Data
@Table(name = "reply")
public class Reply {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")//自定义ID生成器
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "com.cxyzj.cxyzjback.Utils.SnowflakeIdGenerator")
    @Column(name = "reply_id")
    private String replyId;

    @Column(name = "comment_id")
    private String commentId;

    @Column(name = "replier")
    private String replier;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "re_time")
    private long replyTime;

    @Column(name = "text")
    private String text;

    @Column(name = "support")
    private int support = 0;

    @Column(name = "object")
    private int object = 0;

    @Column(name = "target_id")
    private String targetId;

    @Column(name = "mode")
    private String mode;

}
