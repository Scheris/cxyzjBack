package com.cxyzj.cxyzjback.Bean.Article;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Auther: 夏
 * @DATE: 2018/9/18 08:38
 * @Description:
 */

@Entity
@Data
@Table(name = "article_collection")
public class ArticleCollection {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")//自定义ID生成器
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "com.cxyzj.cxyzjback.Utils.SnowflakeIdGenerator")
    @Column(name = "id")
    private String Id;

    @Column(name = "article_id")
    private String articleId;

    @Column(name = "user_id")
    private String userId;

}
