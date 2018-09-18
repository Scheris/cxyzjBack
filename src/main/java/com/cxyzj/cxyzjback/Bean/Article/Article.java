package com.cxyzj.cxyzjback.Bean.Article;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Auther: 夏
 * @DATE: 2018/9/12 09:59
 * @Description:
 */

@Entity
@Data
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")//自定义ID生成器
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "com.cxyzj.cxyzjback.Utils.SnowflakeIdGenerator")
    @Column(name = "article_id")
    private String articleId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "title")
    private String title;

    @Column(name = "update_time")
    private long updateTime;

    @Column(name = "article_sum")
    private String articleSum;

    @Column(name = "type_id")
    private String typeId;

    @Column(name = "views")
    private int views = 0;

    @Column(name = "comments")
    private int comments = 0;

    @Column(name = "collections")
    private int collections = 0;

    @Column(name = "text")
    private String text;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "status_id")
    private int status_id;

    @Column(name = "levels")
    private int levels;

}
