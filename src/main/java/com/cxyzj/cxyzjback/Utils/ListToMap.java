package com.cxyzj.cxyzjback.Utils;

import com.cxyzj.cxyzjback.Bean.Article.Article;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * @Package com.cxyzj.cxyzjback.Utils
 * @Author Yaser
 * @Date 2018/10/12 15:28
 * @Description: 将list数据转换为map形式数据
 */
public class ListToMap<T> {
    /**
     * @param list 要进行转换的列表数据
     * @param key  转换为map后的主键是哪一个
     * @return 转换后的map
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, T> getMap(List<T> list, String key, Class clazz) {

        HashMap<String, T> resultMap = new HashMap<>();
        try {
            Field field = clazz.getDeclaredField(key);//访问私有变量
            field.setAccessible(true);
            for (T aList : list) {
                resultMap.put((String) field.get(aList), aList);//进行映射操作
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}