package com.cxyzj.cxyzjback.Service.impl.Other;

import com.cxyzj.cxyzjback.Bean.Redis.RedisKeyDto;
import com.cxyzj.cxyzjback.Service.Interface.Other.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * @Author 夏
 * @Date 10:38 2018/8/15
 * @Description redis服务类
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisTemplate<Serializable, Serializable> redisTemplate;

    @Override
    public void addData(final RedisKeyDto redisKeyDto) {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.set(
                    redisTemplate.getStringSerializer().serialize(redisKeyDto.getKeys()),
                    redisTemplate.getStringSerializer().serialize(redisKeyDto.getValues())
            );
            return null;
        });
    }

    @Override
    public void delete(final RedisKeyDto redisKeyDto) {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.del(redisTemplate.getStringSerializer().serialize(redisKeyDto.getKeys()));
            return null;
        });
    }

    @Override
    public RedisKeyDto redisGet(final RedisKeyDto redisKeyDto) {
        return redisTemplate.execute((RedisCallback<RedisKeyDto>) connection -> {
            byte[] key = redisTemplate.getStringSerializer().serialize(redisKeyDto.getKeys());
            if (connection.exists(key)) {
                byte[] value = connection.get(key);
                //从redis中取出的需要反序列化--- deserialize
                String redisValue = redisTemplate.getStringSerializer().deserialize(value);
                RedisKeyDto re = new RedisKeyDto();
                re.setKeys(redisKeyDto.getKeys());
                re.setValues(redisValue);
                return re;
            }
            return null;
        });
    }

    @Override
    public void addRedisData(final RedisKeyDto redisKeyDto, final int outTime) {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.set(
                    redisTemplate.getStringSerializer().serialize(redisKeyDto.getKeys()),
                    redisTemplate.getStringSerializer().serialize(redisKeyDto.getValues())
            );
            connection.expire(redisTemplate.getStringSerializer().serialize(redisKeyDto.getKeys()), outTime);
            return null;
        });
    }

    @Override
    public String conn() {
        Jedis jedis = new Jedis("localhost");
        Transaction t = jedis.multi();
        t.exec();
        return jedis.ping();
    }


    public RedisTemplate<Serializable, Serializable> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<Serializable, Serializable> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


}
