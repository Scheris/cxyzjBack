package com.cxyzj.cxyzjback.Bean.Other;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Package com.cxyzj.cxyzjback.Bean.Other
 * @Author Yaser
 * @Date 2018/09/28 14:05
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "filepath")//前缀
@PropertySource(value = "classpath:config/Config.properties")//配置文件路径
@Data
public class ConfigResources {
    private FilePath avatar;
    private FilePath background;
    private FilePath article;
    private FilePath discussion;
    private FilePath other;
    private FilePath temp;
}
