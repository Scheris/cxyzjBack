package com.cxyzj.cxyzjback.Controller;

import com.cxyzj.cxyzjback.Service.ITestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Package com.cxyzj.cxyzjback.Controller
 * @Author Yaser
 * @Date 2018/07/29 15:34
 * @Description: 注意，仅供参考
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1/front/users")
public class TestController {
    @Autowired
    private ITestService userService;

    //返回的数据类型均为string，格式为json
    @PostMapping(value = "/login")
    public String getUser(@RequestParam String id) {
        log.info("/login");
        return userService.findAllByID(id);
    }

    @PostMapping(value = "/register")
    public String addUser(@RequestParam String password, @RequestParam String gender, @RequestParam String email) {
        log.info("/register");
        return userService.addUser(email, password, gender);
    }

    @GetMapping
    public String getUserList() {
        log.info("/users");
        return userService.findAll();
    }
}
