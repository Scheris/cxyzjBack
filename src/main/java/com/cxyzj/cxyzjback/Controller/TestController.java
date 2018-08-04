package com.cxyzj.cxyzjback.Controller;

import com.cxyzj.cxyzjback.Service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Package com.cxyzj.cxyzjback.Controller
 * @Author Yaser
 * @Date 2018/07/29 15:34
 * @Description: 注意，仅供参考，
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/v1/front/users")
public class TestController {
    @Autowired
    private ITestService userService;


    @PostMapping(value = "/login")
    public String getUser(@RequestParam String id) {
        return userService.findAllByID(id);
    }

    @PostMapping(value = "/register")
    public String addUser(@RequestParam String password, @RequestParam String gender, @RequestParam String email) {
        return userService.addUser(email,password,gender);
    }

    @GetMapping
    public String getUserList() {
      return userService.findAll();
    }
}
