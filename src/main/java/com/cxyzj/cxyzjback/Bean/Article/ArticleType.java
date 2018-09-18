package com.cxyzj.cxyzjback.Bean.Article;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Auther: 夏
 * @DATE: 2018/9/17 16:15
 * @Description:
 */

@Entity
@Data
@Table(name = "article_type")
public class ArticleType {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")//自定义ID生成器
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "com.cxyzj.cxyzjback.Utils.SnowflakeIdGenerator")
    @Column(name = "type_id")
    private String typeId;

    @Column(name = "type_name")
    private String typeName;

    private int quantity;

}
