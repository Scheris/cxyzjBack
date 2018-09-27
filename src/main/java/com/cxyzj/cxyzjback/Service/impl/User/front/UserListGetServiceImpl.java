package com.cxyzj.cxyzjback.Service.impl.User.front;


import com.cxyzj.cxyzjback.Bean.User.Attention;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.Other.PageData;
import com.cxyzj.cxyzjback.Data.User.OtherDetails;
import com.cxyzj.cxyzjback.Repository.User.UserAttentionJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.User.front.UserListGetService;
import com.cxyzj.cxyzjback.Utils.Constant;
import com.cxyzj.cxyzjback.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author 夏
 * @Date 15:50 2018/8/30
 */

@Service
public class UserListGetServiceImpl implements UserListGetService {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserAttentionJpaRepository userAttentionJpaRepository;
    private Response response;

    /**
     * @param userId  用户ID
     * @param pageNum 页码（从0开始）
     * @return 关注列表
     * @checked true
     */
    @Override
    public String getAttentionList(String userId, int pageNum) {
        response = new Response();
        Page<Attention> attentionPage = pageSelect(pageNum, Constant.FOCUS, userId);
        PageData pageData = new PageData(attentionPage, pageNum);
        ArrayList<OtherDetails> otherDetailsArrayList = getAttentionsOrFans(attentionPage.iterator(), true);
        response.insert(pageData);
        response.insert("attentions", otherDetailsArrayList);
        return response.sendSuccess();
    }

    /**
     * @param userId  用户ID
     * @param pageNum 页码（从0开始）
     * @return 粉丝列表
     * @checked true
     */
    @Override
    public String getFansList(String userId, int pageNum) {
        response = new Response();
        Page<Attention> attentionPage = pageSelect(pageNum, Constant.FOLLOWED, userId);
        PageData pageData = new PageData(attentionPage, pageNum);
        ArrayList<OtherDetails> otherDetailsArrayList = getAttentionsOrFans(attentionPage.iterator(), false);
        response.insert(pageData);
        response.insert("fans", otherDetailsArrayList);
        return response.sendSuccess();
    }

    /**
     * @param pageNum 指定的页数
     * @param status  指定状态
     * @param userId  指定要查询的用户id
     * @return 分页查询结果
     */
    private Page<Attention> pageSelect(int pageNum, int status, String userId) {
        Sort sort = new Sort(Sort.DEFAULT_DIRECTION, "userId");
        Pageable pageable = PageRequest.of(pageNum, Constant.PAGE_ATTENTION_USER, sort);
        return userAttentionJpaRepository.findAllByUserIdAndStatus(pageable, userId, status);
    }

    /**
     * @param attentionIterator 要处理的用户的迭代器
     * @param isAttention       是否是关注
     * @return 处理后的用户信息
     */
    private ArrayList<OtherDetails> getAttentionsOrFans(Iterator<Attention> attentionIterator, boolean isAttention) {
        ArrayList<String> userIdList = new ArrayList<>();
        while (attentionIterator.hasNext()) {
            userIdList.add(attentionIterator.next().getTargetUser());
        }
        List<User> users = userJpaRepository.findByUserId(userIdList);
        ArrayList<OtherDetails> otherDetailsArrayList = new ArrayList<>();
        for (User user : users) {
            otherDetailsArrayList.add(new OtherDetails(user, isAttention));
        }
        return otherDetailsArrayList;
    }
}
