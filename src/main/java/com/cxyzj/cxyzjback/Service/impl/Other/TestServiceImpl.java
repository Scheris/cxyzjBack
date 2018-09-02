package com.cxyzj.cxyzjback.Service.impl.Other;

import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.User.UserDetails;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Other.ITestService;
import com.cxyzj.cxyzjback.Service.Interface.User.AuthService;
import com.cxyzj.cxyzjback.Service.impl.User.AuthServiceImpl;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Package com.cxyzj.cxyzjback.Service.impl
 * @Author Yaser
 * @Date 2018/07/29 15:29
 * @Description:
 */
@Service
@Transactional
public class TestServiceImpl implements ITestService {
    @Autowired
    private UserJpaRepository userJpaRepository;
    private Response response;

    @Override
    public String findAll() {
        response = new Response();
        User user[] = userJpaRepository.findAll().toArray(new User[0]);
        response.insert(user);
        return response.sendSuccess();
    }

    @Override
    public String findByID(String ID) {
        response = new Response();
        User user = userJpaRepository.findByUserId(ID);
        if (user != null) {
            response.insert(new UserDetails(user));
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.NONE_USER,"用户不存在");
        }
    }

    @Override
    public String addUser(String email, String password, int gender, String nickname, String phone, String headUrl) {
        User user = new User();
        user.setEmail(email);
        user.setGender(gender);
        user.setPassword(password);
        response.insert(userJpaRepository.save(user));
        return response.sendSuccess();
    }

    public static void main(String[] args) throws NoSuchFieldException {
        AuthService test = new AuthServiceImpl();
        test.register("嘿嘿","123456","123456",0,"123456789","iojdosdfsdfsdf");
//        ystem.out.println(result);
    }
}
