package com.cxyzj.cxyzjback.Utils;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Package com.cxyzj.cxyzjback.Utils
 * @Author Yaser
 * @Date 2018/07/29 9:24
 * @Description: 自建数据结构，用于返回数据
 */
class Item<type> {
    private String name;//名字
    private ArrayList<type> items = new ArrayList<>();//数组型数据

    String getName() {
        return name;
    }

    void insert(String name, type[] t) {//将数组插入到数据中
        this.name = name;
        items.addAll(Arrays.asList(t));
    }

    void insert(String name, type t) {//插入单条数据
        this.name = name;
        items.add(t);
    }

    ArrayList<type> getItems() {
        return items;
    }

}
