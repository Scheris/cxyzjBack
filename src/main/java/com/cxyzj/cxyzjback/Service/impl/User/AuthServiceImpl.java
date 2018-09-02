package com.cxyzj.cxyzjback.Service.impl.User;

import com.cxyzj.cxyzjback.Bean.Redis.RedisKeyDto;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.User.UserBasic;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Other.RedisService;
import com.cxyzj.cxyzjback.Service.Interface.User.AuthService;
import com.cxyzj.cxyzjback.Utils.Code;
import com.cxyzj.cxyzjback.Utils.CodeSend;
import com.cxyzj.cxyzjback.Utils.JWT.JWTUtils;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.util.Date;

/**
 * @Package com.cxyzj.cxyzjback.Service.impl
 * @Author Yaser
 * @Date 2018/08/10 15:38
 * @Description:
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserJpaRepository userJpaRepository;
    private Response response;

    //短信验证码过期时间
    private  static int  EXPIRATIONTIME=3600*1000*24;

    @Resource
    private RedisService redisService;


    @Override
    public String login(String email, String phone, String password) throws NoSuchFieldException {
        JWTUtils jwtUtils = new JWTUtils();
        User user;
        RedisKeyDto redisKeyDto = new RedisKeyDto();

        if (email != null) {
            user = userJpaRepository.findByEmailAndPassword(email, password);//读取信息

        } else {
            if (phone == null) {
                throw new NoSuchFieldException("phone字段与email字段不可同时为空！");
            } else {
                user = userJpaRepository.findByPhoneAndPassword(phone, password);//读取信息
            }
        }
        response = new Response();
        if (user != null) {
            String token = jwtUtils.generateToken(user);//生成用户token
            log.info("token:  " + token);
            //注意：后端从数据库中查找到的数据不可以直接返回，需要重新自定义一个数据结构！
            UserBasic loginResult = new UserBasic(user);//转换返回的数据为前端需要的数据
            response.insert("token", token);
            response.insert("user", loginResult);
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.NONE_USER, "用户名或密码错误！");
        }

    }

    @Override
    public String refresh(String oldToken) {
        return null;
    }

    @Override
    public String register(String nickname, String email, String password, int gender, String phone, String headUrl) {

        //注册时间
        DateFormat dateTime = DateFormat.getDateTimeInstance();
        String dt = dateTime.format(new Date());

        JWTUtils jwtUtils = new JWTUtils();
        User user = new User();
        response = new Response();
        if(userJpaRepository.findByEmail(email) != null){
            return response.sendFailure(Status.EMAIL_HAS_REGISTER,"邮箱已经使用过了哦！换个邮箱试试？");

        }else if(userJpaRepository.findByPhone(phone) != null){
            return response.sendFailure(Status.PHONE_HAS_REGISTER,"手机号码已经使用过了哦！换个号码试试？");
        }else if(userJpaRepository.findByNickname(nickname) != null){
            return response.sendFailure(Status.NICKNAME_EXIST,"昵称已被征用了哦，再换个试试？");
        }else{

            user.setNickname(nickname);
            user.setEmail(email);
            user.setPassword(password);
            user.setGender(gender);
            user.setPhone(phone);
            user.setRegistDate(dt);
            user.setHeadUrl(headUrl);
            user.setRoleId(1);
            userJpaRepository.save(user);

            String token = jwtUtils.generateToken(user);//生成用户token
            response.insert("token", token);
            response.insert("user", new UserBasic(user));
            return response.sendSuccess();
        }
    }

    @Override
    public String sendCode(String email, String phone) throws Exception {

        RedisKeyDto redisKeyDto=new RedisKeyDto();
        response = new Response();
        CodeSend codeSend = new CodeSend();

        if(email != null){
            String code = Code.mailCode();
            redisKeyDto.setKeys(email);
            redisKeyDto.setValues(code);
            redisService.addRedisData(redisKeyDto,EXPIRATIONTIME);
            return codeSend.mailSend(email,code);
        }else{
            if (phone != null) {
                String code = Code.phoneCode();
                redisKeyDto.setKeys(phone);
                redisKeyDto.setValues(code);
                redisService.addRedisData(redisKeyDto,EXPIRATIONTIME);
                return codeSend.phoneSend(phone, code);
            }else {
                throw new NoSuchFieldException("手机和邮箱不能同时为空!");
            }
        }
    }

    @Override
    public String verifyCode(String phone, String email, String code) throws NoSuchFieldException {
        response = new Response();
        RedisKeyDto redisKeyDto=new RedisKeyDto();
        if(email == null){
            if(phone !=  null){
                redisKeyDto.setKeys(phone);
                RedisKeyDto result=redisService.redisGet(redisKeyDto);
                if (result!=null&&result.getValues().equals(String.valueOf(code)))
                    return response.sendSuccess();
                return response.sendFailure(Status.CODE_ERROR,"验证码错误");
            }else {
                throw new NoSuchFieldException("手机和邮箱不能同时为空！");
            }
        }else{
            redisKeyDto.setKeys(email);
            RedisKeyDto result = redisService.redisGet(redisKeyDto);
            if(result != null&&result.getValues().equals(String.valueOf(code)))
                return response.sendSuccess();
            return response.sendFailure(Status.CODE_ERROR,"验证码错误");
        }

    }

    @Override
    public String loginCode(String phone, String code) throws NoSuchFieldException {

        RedisKeyDto redisKeyDto=new RedisKeyDto();
        response = new Response();
        JWTUtils jwtUtils = new JWTUtils();
        User user;
        if(phone != null){
            redisKeyDto.setKeys(phone);
            RedisKeyDto result = redisService.redisGet(redisKeyDto);
            if(result != null && result.getValues().equals(String.valueOf(code))){
                user = userJpaRepository.findByPhone(phone);
                if (user != null) {
                    String token = jwtUtils.generateToken(user);
                    log.info("token:  " + token);
                    UserBasic loginResult = new UserBasic(user);
                    response.insert("token", token);
                    response.insert("user", loginResult);
                    return response.sendSuccess();
                } else {
                    return response.sendFailure(Status.NONE_USER, "用户不存在！");
                }
            }else {
                return response.sendFailure(Status.CODE_ERROR,"验证码错误！");
            }
        }else{
            throw new NoSuchFieldException("请输入手机号！");
        }
    }

    @Override
    public String forgetPassword(String email, String phone, String password, String code) throws NoSuchFieldException {

        RedisKeyDto redisKeyDto = new RedisKeyDto();
        response = new Response();
        User user;
        if(phone == null){
            if(email != null){
                redisKeyDto.setKeys(email);
                RedisKeyDto result = redisService.redisGet(redisKeyDto);
                if(result != null && result.getValues().equals(String.valueOf(code))){
                    userJpaRepository.updatePasswordByEmail(password, email);
                    return response.sendSuccess();
                }else{
                    return response.sendFailure(Status.CODE_ERROR,"验证码错误！");
                }
            }else{
                throw new NoSuchFieldException("邮箱和手机不能同时为空！");
            }
        }else{
            redisKeyDto.setKeys(phone);
            RedisKeyDto result = redisService.redisGet(redisKeyDto);
            if(result != null && result.getValues().equals(String.valueOf(code))){
                userJpaRepository.updatePasswordByPhone(password, phone);
                return response.sendSuccess();
            }else{
                return response.sendFailure(Status.CODE_ERROR,"验证码错误！");
            }
        }
    }
}
