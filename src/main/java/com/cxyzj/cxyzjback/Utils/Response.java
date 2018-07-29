package com.cxyzj.cxyzjback.Utils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author wanyu
 * @Date: 2018-01-18
 * @Time: 19:09
 * To change this template use File | Settings | File Templates.
 * @desc 响应请求
 */
public class Response {
    private int status;
    private HashMap<String, ArrayList> data = new HashMap<>();
    private HashMap<String, Object> response = new HashMap<>();


    public void insert(Item item) {
        data.put(item.getName(), item.getItems());
    }

    public String SendSuccess() {
        status = 0;
        response.put("status", status);
        response.put("data", data);
        return new Gson().toJson(response, HashMap.class);
    }

    public String SendFailure(String statusInfo) {
        status = 1;
        response.put("status", status);
        response.put("statusInfo", statusInfo);
        return new Gson().toJson(response, HashMap.class);
    }

}

