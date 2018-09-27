package com.cxyzj.cxyzjback.Controller.User.front;


import com.cxyzj.cxyzjback.Service.Interface.User.front.AuthService;
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
        log.info("login");
        try {
            return authService.login(email, phone, password);
        } catch (NoSuchFieldException e) {
            return new Response().sendFailure(Status.NO_SUCH_FIELD, "email字段与phone字段不可以同时为空");
        }
    }


    @PostMapping(value = "/register")
    public String register(String nickname, String email, String password, int gender, String phone, String head_url) {
        return authService.register(nickname, email, password, gender, phone, head_url);

    }

    //验证码发送
    @PostMapping(value = "/send_code")
    public String sendCode(@RequestParam(required = false) String email, @RequestParam(required = false) String phone) {
        try {
            return authService.sendCode(email, phone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response().sendFailure(Status.NO_SUCH_FIELD, "邮箱和手机不能同时为空！");
    }

    @GetMapping(value = "/exist")
    public String existUser(@RequestParam(required = false) String email, @RequestParam(required = false) String phone, @RequestParam(required = false) String nickname) {
        try {
            return authService.existUser(email, phone, nickname);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return new Response().sendFailure(Status.NO_SUCH_FIELD, "邮箱，手机和昵称不能同时为空！");
    }

    //验证码校验
    @PostMapping(value = "/verify_code")
    public String verifyCode(@RequestParam(required = false) String phone, @RequestParam(required = false) String email,
                             @RequestParam String code) {
        try {
            return authService.verifyCode(phone, email, code);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return new Response().sendFailure(Status.NO_SUCH_FIELD, "邮箱和手机不能同时为空！");

    }

    //验证码登陆
    @PostMapping(value = "/login_code")
    public String loginCode(@RequestParam String phone, @RequestParam String code) {
        return authService.loginCode(phone, code);
    }

    //忘记密码，重新设置
    @PostMapping(value = "/forget_password")
    public String forgetPassword(@RequestParam(required = false) String email, @RequestParam(required = false) String phone,
                                 @RequestParam String password, @RequestParam String code) {
        try {
            return authService.forgetPassword(email, phone, password, code);
        } catch (NoSuchFieldException e) {
            return new Response().sendFailure(Status.NO_SUCH_FIELD, "邮箱和手机不能同时为空！");
        }
    }

}
