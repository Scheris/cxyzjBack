package com.cxyzj.cxyzjback.Utils;


import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author wanyu
 * @Date: 2018-01-18
 * @Time: 19:09
 * To change this template use File | Settings | File Templates.
 * @desc 相应请求
 */
public abstract class Response {


    public void insert(String name, Object data) {
    }

    public void insert(Object data) {

    }

    public abstract Map<String, Object> get();//必须实现
}

