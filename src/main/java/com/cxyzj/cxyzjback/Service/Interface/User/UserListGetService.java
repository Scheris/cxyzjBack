package com.cxyzj.cxyzjback.Service.Interface.User;

import com.cxyzj.cxyzjback.Bean.PageBean;
import com.cxyzj.cxyzjback.Bean.User.Attention;

/**
 * @Author 夏
 * @Date 15:50 2018/8/30
 */
public interface UserListGetService {
    String getAttentionList(int pageNum);

    String getFansList(int pageNum);
}
