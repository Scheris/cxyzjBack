package com.cxyzj.cxyzjback.Utils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
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
    private HashMap<String, Object> data = new HashMap<>();//数据
    private HashMap<String, Object> response = new HashMap<>();//响应

    public void insert(Object bean) {//插入单个对象数据
        insert(bean.getClass().getSimpleName().toLowerCase(), bean);
    }

    public void insert(String key, Object value) {//插入单条数据
        data.put(key, value);
    }

    public void insert(Object[] beans) {//插入多个对象数据
        ArrayList<Object> tmp = new ArrayList<>(Arrays.asList(beans));
        data.put(beans[0].getClass().getSimpleName().toLowerCase(), tmp);
    }

    public String SendSuccess() {
        response.put("status", Status.OK);
        response.put("data", data);
        return new Gson().toJson(response, HashMap.class);//格式转换
    }

    public String SendFailure(String statusInfo) {
        response.put("status", Status.FAILURE);
        response.put("statusInfo", statusInfo);
        return new Gson().toJson(response, HashMap.class);//格式转换
    }

}

