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
@Table(name = "article_label")
public class ArticleLabel {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")//自定义ID生成器
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "com.cxyzj.cxyzjback.Utils.SnowflakeIdGenerator")
    @Column(name = "label_id")
    private String labelId;
    @Column(name = "label_name")
    private String labelName;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "thumb")
    private String thumb;
    @Column(name = "collections")
    private int collections;
    @Column(name = "introduce")
    private String introduce;

}
