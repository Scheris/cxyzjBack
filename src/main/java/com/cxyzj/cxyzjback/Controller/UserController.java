package com.cxyzj.cxyzjback.Controller;

import com.cxyzj.cxyzjback.Bean.User;
import com.cxyzj.cxyzjback.Service.IUserService;
import com.cxyzj.cxyzjback.Utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package com.cxyzj.cxyzjback.Controller
 * @Author Yaser
 * @Date 2018/07/29 15:34
 * @Description:
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private IUserService userService;
    private Response response;
    private String result;

    @GetMapping(value = "/{id}")
    public String getUser(@PathVariable String id) {
        response = new Response();
        User user = userService.findAllByID(id);

        if (user != null) {
            response.insert(user);
            response.insert("hello", 1212);
            result = response.SendSuccess();
        } else {
            result = response.SendFailure("没有该用户");
        }

        return result;
    }

    @GetMapping
    public String getUserList() {
        response = new Response();
        User user[] = userService.findAll().toArray(new User[0]);
        if (user.length != 0) {
            response.insert(user);
            result = response.SendSuccess();
        } else {
            result = response.SendFailure("还没有用户");
        }

        return result;
    }
}
