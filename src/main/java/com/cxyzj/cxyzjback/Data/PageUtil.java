package com.cxyzj.cxyzjback.Data;

import com.cxyzj.cxyzjback.Bean.PageBean;

/**
 * @Author 夏
 * @Date 17:16 2018/8/30
 */
public class PageUtil {

    private boolean is_end;
    private int page_num;
    private int total;

    public PageUtil(PageBean pageBean){
        this.is_end = pageBean.getPageNum() == pageBean.getEnd();
        this.page_num = pageBean.getPageNum();
        this.total = pageBean.getTotalPage();
    }

}
