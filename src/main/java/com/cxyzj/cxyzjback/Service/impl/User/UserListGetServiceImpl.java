package com.cxyzj.cxyzjback.Service.impl.User;

import com.cxyzj.cxyzjback.Bean.PageBean;
import com.cxyzj.cxyzjback.Bean.Redis.RedisKeyDto;
import com.cxyzj.cxyzjback.Bean.User.Attention;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.PageUtil;
import com.cxyzj.cxyzjback.Data.User.OtherDetails;
import com.cxyzj.cxyzjback.Data.User.OtherSimple;
import com.cxyzj.cxyzjback.Data.User.UserList;
import com.cxyzj.cxyzjback.Repository.User.UserAttentionJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.User.UserListGetService;
import com.cxyzj.cxyzjback.Utils.Response;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private String userId;
    private User user;

    @Override
    public String getAttentionList(int pageNum) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Attention> allAttention = userAttentionJpaRepository.findByUserId(userId);

        int totalRecord = allAttention.size();
        PageBean pb = new PageBean();
        pb.PageBean(pageNum, 9, totalRecord);

        int startIndex = pb.getStartIndex();
        Attention attention[] = userAttentionJpaRepository.findAttention(startIndex, 9, userId);

        pb.setList(userAttentionJpaRepository.findAll(startIndex, 9));
        pb.setPageNum(pageNum);

        List<UserList> userLists = new ArrayList<UserList>();
        for(int i = 0; i<attention.length; i++){
            user = userJpaRepository.findByUserId(attention[i].getTargetUser());
            UserList userList = new UserList(user);
            userList.set_followed(true);
            userLists.add(userList);
        }

        response.insert("attentions", userLists);
        response.insert("page", new PageUtil(pb));
        return response.sendSuccess();
    }

    @Override
    public String getFansList(int pageNum) {

        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Attention> allAttention = userAttentionJpaRepository.findByUserId(userId);
        int totalRecord = allAttention.size();

        PageBean pb = new PageBean();
        pb.PageBean(pageNum, 9, totalRecord);

        int startIndex = pb.getStartIndex();
        Attention attention[] = userAttentionJpaRepository.findFans(startIndex, 9, userId);

        pb.setList(userAttentionJpaRepository.findAll(startIndex, 9));
        pb.setPageNum(pageNum);

        List<UserList> userLists = new ArrayList<UserList>();
        for(int i = 0; i<attention.length; i++){
            int status = userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, attention[i].getUserId());
            UserList userList = new UserList(userJpaRepository.findByUserId(attention[i].getUserId()));
            if(status != 203){
                userList.set_followed(false);
            }else {
                userList.set_followed(true);
            }
            userLists.add(userList);
        }

        response.insert("attentions", userLists);
        response.insert("page", new PageUtil(pb));
        return response.sendSuccess();

    }
}
