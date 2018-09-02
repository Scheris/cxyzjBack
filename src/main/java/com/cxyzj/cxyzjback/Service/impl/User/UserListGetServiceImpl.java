package com.cxyzj.cxyzjback.Service.impl.User;

import com.cxyzj.cxyzjback.Bean.PageBean;
import com.cxyzj.cxyzjback.Bean.Redis.RedisKeyDto;
import com.cxyzj.cxyzjback.Bean.User.Attention;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.PageUtil;
import com.cxyzj.cxyzjback.Data.User.OtherDetails;
import com.cxyzj.cxyzjback.Data.User.OtherSimple;
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
    RedisKeyDto redisKeyDto;
    private Response response;
    private String userId;
    private User user;

    @Override
    public String getAttentionList(int pageNum) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();

        JSONArray jsonArray = new JSONArray();

        List<Attention> allAttention = userAttentionJpaRepository.findByUserId(userId);
        int totalRecord = allAttention.size();

        PageBean pb = new PageBean();
        pb.PageBean(pageNum, 9, totalRecord);
        int startIndex = pb.getStartIndex();

        Attention attention = userAttentionJpaRepository.findAttention(startIndex, 9, userId);
        int status = userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, attention.getTargetUser());
        user = userJpaRepository.findByUserId(attention.getTargetUser());

        OtherDetails otherDetails = new OtherDetails(user);
        if(status == 201 || status == 203){
            otherDetails.set_followed(true);
            response.insert("attention", otherDetails);
        }else{
            response.insert("attention", otherDetails);
        }

        pb.setList(userAttentionJpaRepository.findAll(startIndex, 9));

        pb.setPageNum(pageNum);


        response.insert("page", new PageUtil(pb));
        return response.sendSuccess();
    }
}
