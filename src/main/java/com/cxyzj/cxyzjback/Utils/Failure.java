package com.cxyzj.cxyzjback.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author wanyu
 * @Date: 2018-01-18
 * @Time: 19:35
 * To change this template use File | Settings | File Templates.
 * @desc 失败
 */
public class Failure extends Response {
    private static final int status = 1;
    private Map<String, Object> statusInfo = new HashMap<>();
    private Map<String, Object> res = new HashMap<>();

    public Failure(String message) {
        statusInfo.put("message", message);
        res.put("status", status);
        res.put("statusInfo", statusInfo);
    }


    @Override
    public Map<String, Object> get() {
        return res;
    }
}
