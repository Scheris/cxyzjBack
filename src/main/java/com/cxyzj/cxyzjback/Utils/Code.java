package com.cxyzj.cxyzjback.Utils;

import java.util.Random;


/**
 * 验证码生成工具
 * @author 夏
 *
 */
public class Code {

    /**
     * 随机字符组成的邮箱验证码
     *
     */
    public static String mailCode(){
        char[] ch = "abcdefghijkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXY3456789".toCharArray();

        Random r = new Random();
        int len = ch.length;
        int index;
        StringBuffer sb = new StringBuffer();
        for(int i = 0;i<6;i++){
            index = r.nextInt(len);
            sb.append(ch[index]);
        }
        return sb.toString();
    }

    /**
     * 随机六位数的手机验证码
     *
     */
    public static String  phoneCode(){
        char[] ch = "0123456789".toCharArray();

        Random r = new Random();
        int len = ch.length;
        int index;
        StringBuffer sb = new StringBuffer();
        for(int i = 0;i<6;i++){
            index = r.nextInt(len);
            sb.append(ch[index]);
        }
        return sb.toString();  //每次调用生成一次六位数的随机数
    }

}
