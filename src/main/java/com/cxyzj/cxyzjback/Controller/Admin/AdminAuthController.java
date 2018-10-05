package com.cxyzj.cxyzjback.Controller.Admin;

import com.cxyzj.cxyzjback.Service.Interface.User.front.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: 夏
 * @DATE: 2018/9/29 10:17
 * @Description:
 */

@Controller
@CrossOrigin
@Slf4j
@RequestMapping("/v1/admin/auth")
public class AdminAuthController {

    @PostMapping(value = "/login")
    public String adminLogin(@RequestParam(required = false) String email, @RequestParam(required = false) String phone, @RequestParam String password){
        return null;
    }

}
