package com.cxyzj.cxyzjback.Controller.User.front;

import com.cxyzj.cxyzjback.Service.Interface.User.front.UserListGetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Author 夏
 * @Date 15:51 2018/8/30
 */

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1/user")
public class UserListGetController {

    @Autowired
    UserListGetService userListGetService;

    @GetMapping(value = "{user_id}/attention_list/{page_num}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getAttentionList(@PathVariable(name = "user_id") String userId, @PathVariable(name = "page_num") int pageNum) {
        return userListGetService.getAttentionList(userId, pageNum);
    }

    @GetMapping(value = "{user_id}/fans_list/{page_num}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getFansList(@PathVariable(name = "user_id") String userId, @PathVariable(name = "page_num") int pageNum) {
        return userListGetService.getFansList(userId, pageNum);
    }

    @GetMapping(value = "/{user_id}/article_list/{page_num}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getArticleList(@PathVariable(name = "user_id") String userId, @PathVariable(name = "page_num") int pageNum){
        return userListGetService.getArticleList(userId, pageNum);
    }

    @GetMapping(value = "/{user_id}/comment_list/{page_num}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String userComments(@PathVariable(name = "user_id") String user_id, @PathVariable(name = "page_num") int pageNum) {
        return userListGetService.userComments(user_id, pageNum);
    }


}
