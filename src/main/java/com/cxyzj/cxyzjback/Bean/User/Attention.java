package com.cxyzj.cxyzjback.Bean.User;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @Package com.cxyzj.cxyzjback.Template.User
 * @Author Yaser
 * @Date 2018/08/04 12:24
 * @Description:
 */
@Entity
@Data
@Table(name = "attention")
public class Attention  {
    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")//自定义ID生成器
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "com.cxyzj.cxyzjback.Utils.SnowflakeIdGenerator")
    @Column(name = "attention_id")
    private String attentionId;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "target_user")
    private String targetUser;
    @Column(name = "status_id")
    private int status;
}
