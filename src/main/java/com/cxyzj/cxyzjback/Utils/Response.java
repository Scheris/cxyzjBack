package com.cxyzj.cxyzjback.Utils;

import com.google.gson.Gson;
import com.cxyzj.cxyzjback.Bean.*;


import java.lang.reflect.Constructor;
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
    private int status;//状态码
    private HashMap<String, Object> data = new HashMap<>();//数据
    private HashMap<String, Object> response = new HashMap<>();//响应

    public void insert(Bean bean) {//插入单个对象数据
        insert(bean.getClassName(), bean);
    }

    public void insert(String key, Object value) {//插入单条数据
        data.put(key,value);
    }

    public void insert(Bean[] beans) {//插入数组型数据
        if(beans.length>0){
            ArrayList<Bean> tmp = new ArrayList<>(Arrays.asList(beans));
            data.put(beans[0].getClassName(),tmp);
        }else {
            //todo 使用日志输出
        }

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

