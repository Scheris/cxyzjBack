package com.cxyzj.cxyzjback.Bean;

/**
 * @Package com.cxyzj.cxyzjback.Bean
 * @Author Yaser
 * @Date 2018/07/29 9:57
 * @Description:
 */
public class Student {
    private String name;
    private int age;
    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public static String getClassName(){
        return "Student";
    }
}
