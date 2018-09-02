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
        if(pageBean.getPageNum() == pageBean.getEnd()){
            this.is_end = true;
        }else {
            this.is_end = false;
        }
        this.page_num = pageBean.getPageNum();
        this.total = pageBean.getTotalPage();
    }

}
