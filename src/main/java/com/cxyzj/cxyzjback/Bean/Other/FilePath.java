package com.cxyzj.cxyzjback.Bean.Other;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Package com.cxyzj.cxyzjback.Bean.Other
 * @Author Yaser
 * @Date 2018/09/28 14:18
 * @Description:
 */

@Component//使用@Configuration也可以
@Data
public class FilePath {
    private String absolute;
    private String relative;

    public FilePath(FilePath filePath) {
        this.absolute = filePath.getAbsolute();
        this.relative = filePath.getRelative();
    }

    public FilePath() {
    }
}