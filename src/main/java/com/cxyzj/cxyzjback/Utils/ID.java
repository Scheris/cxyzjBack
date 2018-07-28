package com.cxyzj.cxyzjback.Utils;

/**
 * Created with IntelliJ IDEA.
 *
 * @author wanyu
 * @Date: 2018-01-22
 * @Time: 19:50
 * To change this template use File | Settings | File Templates.
 * @desc id获取,通过单例模式进行获取
 */
public class ID {
    private static SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(0, 0);

    public static long Get() {
        return snowflakeIdGenerator.nextId();
    }
}
