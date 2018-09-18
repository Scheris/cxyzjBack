package com.cxyzj.cxyzjback.Bean.Article;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Auther: 夏
 * @DATE: 2018/9/6 15:02
 * @Description:
 */

@Entity
@Data
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")//自定义ID生成器
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "com.cxyzj.cxyzjback.Utils.SnowflakeIdGenerator")
    @Column(name = "comment_id")
    private String commentId;

    @Column(name = "discusser")
    private String discusser;

    @Column(name = "text")
    private String text;

    @Column(name = "create_time")
    private long createTime;

    @Column(name = "support")
    private int support = 0;

    @Column(name = "object")
    private int object = 0;

    @Column(name = "target_id")
    private String targetId;

    @Column(name = "mode")
    private String mode;

    @Column(name = "level")
    private int level;

    @Column(name = "children")
    private int children = 0;


}
