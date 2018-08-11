package com.cxyzj.cxyzjback.Controller;

import com.cxyzj.cxyzjback.Service.AuthService;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Package com.cxyzj.cxyzjback.Controller
 * @Author Yaser
 * @Date 2018/08/10 17:53
 * @Description:
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1/user/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login_password")
    public String login(@RequestParam(required = false) String email, @RequestParam String password, @RequestParam(required = false) String phone) {
        try {
            return authService.login(email, phone, password);
        } catch (NoSuchFieldException e) {
            return new Response().sendFailure(Status.NO_SUCH_FIELD, "email字段与phone字段不可以同时为空");
        }
    }
}
