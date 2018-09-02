package com.cxyzj.cxyzjback.Bean;

import lombok.Data;

import java.util.List;

/**
 * @Author 夏
 * @Date 16:31 2018/8/30
 */

@Data
public class PageBean<T> {

    private int pageNum;
    private int pageSize;
    private int totalRecord;

    private int totalPage;
    private int startIndex;

    private boolean is_end;

    private List<T> list;

    private int start;
    private int end;

    public String PageBean(int pageNum, int pageSize, int totalRecord) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;

        if(totalRecord%pageSize == 0){
            this.totalPage = totalRecord / pageSize;
        } else {
            this.totalPage = totalRecord / pageSize + 1;
        }
        this.startIndex = (pageNum - 1)*pageSize;
        this.start = 1;
        this.end = 5;

        if(totalPage <= 5){
            this.end = this.totalPage;
        }else{
            this.start = pageNum - 2;
            this.end = pageNum + 2;

            if(start <= 0){
                this.start = 1;
                this.end = 5;
            }
            if(end > this.totalPage){
                this.end = totalPage;
                this.start = end - 5;
            }
        }
        return null;
    }



}
