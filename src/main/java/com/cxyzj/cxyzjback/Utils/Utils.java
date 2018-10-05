package com.cxyzj.cxyzjback.Utils;




/**
 * @Package com.cxyzj.cxyzjback.Utils
 * @Author Yaser
 * @Date 2018/09/19 11:27
 * @Description: 工具类
 */


public class Utils {

    /**
     * @param str     传入的手机或邮箱
     * @param isPhone 是否是手机
     * @return 返回对邮箱或手机进行掩码操作后的邮箱或手机
     */
    public String maskEmailPhone(String str, boolean isPhone) {
        StringBuilder sb = new StringBuilder(str);
        int end;
        int start = 3;
        if (isPhone) {
            end = str.length() - 2;
        } else {
            end = str.indexOf('@');
        }
        if (end < start) {
            start = end;
        }
        StringBuilder maskCode = new StringBuilder();
        for (int i = start; i < end; i++) {
            maskCode.append("*");
        }
        sb.replace(start, end, maskCode.toString());
        return sb.toString();
    }


}
