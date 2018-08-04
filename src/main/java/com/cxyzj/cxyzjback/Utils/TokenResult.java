package com.cxyzj.cxyzjback.Utils;

import com.cxyzj.cxyzjback.Bean.Template;
import lombok.Data;

/**
 * @Package com.cxyzj.cxyzjback.Utils
 * @Author Yaser
 * @Date 2018/08/04 12:12
 * @Description:
 */
@Data
public class TokenResult implements Template {
    //todo 待实现
    private boolean status;//表示结果成功还是失败

    private String token;

    @Override
    public String getClassName() {
        return "token";
    }
}
