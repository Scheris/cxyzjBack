package com.cxyzj.cxyzjback.Data.Other;

import com.cxyzj.cxyzjback.Data.Data;
import org.springframework.data.domain.Page;

/**
 * @Author 夏
 * @Date 17:16 2018/8/30
 */
public class PageData implements Data {

    private boolean is_end;
    private int page_num;
    private int total;

    public PageData(Page page, int now_page) {
        this.total = page.getTotalPages();
        this.is_end = page.getTotalPages() <= now_page + 1;//是否结束
        this.page_num = now_page;
    }

    @Override
    public String getName() {
        return "page";
    }
}
