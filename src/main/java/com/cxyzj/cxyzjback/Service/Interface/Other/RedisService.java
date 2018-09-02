package com.cxyzj.cxyzjback.Service.Interface.Other;

import com.cxyzj.cxyzjback.Bean.Redis.RedisKeyDto;

/**
 * @Author 夏
 * @Date 10:38 2018/8/15
 */
public interface RedisService {

    void addData(RedisKeyDto redisKeyDto);

    void delete(RedisKeyDto redisKeyDto);

    RedisKeyDto redisGet(RedisKeyDto redisKeyDto);

    void addRedisData(RedisKeyDto redisKeyDto, int outTime);

    String conn();

}
