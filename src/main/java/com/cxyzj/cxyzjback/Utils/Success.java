package com.cxyzj.cxyzjback.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author wanyu
 * @Date: 2018-01-18
 * @Time: 19:31
 * To change this template use File | Settings | File Templates.
 * @desc 成功
 */
public class Success extends Response {
    private static final int status = 0;
    private Map<String, Object> data = new HashMap<>();
    private Map<String, Object> res = new HashMap<>();

    public Success() {
        res.put("status", status);
        res.put("data", data);
    }

    public void insert(String name, Object data) {
        this.data.put(name, data);
    }

    public void insert(Object data) {
        res.put("data", data);
    }

    @Override
    public Map<String, Object> get() {
        return res;
    }

}
