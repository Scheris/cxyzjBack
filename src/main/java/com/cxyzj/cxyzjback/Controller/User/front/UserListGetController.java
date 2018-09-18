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

    @GetMapping(value = "/attention_list/{page_num}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getAttentionList(@PathVariable(name = "page_num") int pageNum){
        return userListGetService.getAttentionList(pageNum);
    }

    @GetMapping(value = "/fans_list/{page_num}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getFansList(@PathVariable(name = "page_num") int pageNum){
        return userListGetService.getFansList(pageNum);
    }

}
