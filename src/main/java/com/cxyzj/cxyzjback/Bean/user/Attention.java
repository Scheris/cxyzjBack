package com.cxyzj.cxyzjback.Bean.user;

import com.cxyzj.cxyzjback.Bean.Template;
import lombok.Data;

import javax.persistence.*;

/**
 * @Package com.cxyzj.cxyzjback.Template.user
 * @Author Yaser
 * @Date 2018/08/04 12:24
 * @Description:
 */
@Entity
@Data
@Table(name = "attention")
public class Attention implements Template {
    @Id
    @Column(name = "attention_id")
    private String attentionId;

    @Column(name="user_id")
    @Override
    public String getClassName() {
        return "attention";
    }
}
