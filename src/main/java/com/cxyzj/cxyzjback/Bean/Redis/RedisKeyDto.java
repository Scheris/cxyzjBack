package com.cxyzj.cxyzjback.Bean.Redis;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author 夏
 * @Date 10:18 2018/8/15
 */
@Data
public class RedisKeyDto implements Serializable {

    private static final long serialVersionUID = 8848809780626060827L;
    private String keys;
    private String values;

}
