package com.cxyzj.cxyzjback.Service.impl.User.front;

import com.cxyzj.cxyzjback.Bean.Redis.RedisKeyDto;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.User.UserBasic;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Other.RedisService;
import com.cxyzj.cxyzjback.Service.Interface.User.front.AuthService;
import com.cxyzj.cxyzjback.Utils.Code;
import com.cxyzj.cxyzjback.Utils.CodeSend;
import com.cxyzj.cxyzjback.Utils.JWT.JWTUtils;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


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

    //短信验证码过期时间,10分钟
    private static final int EXPIRATIONTIME = 60 * 1000 * 10;

    @Resource
    private RedisService redisService;


    @Override
    public String login(String email, String phone, String password) throws NoSuchFieldException {
        JWTUtils jwtUtils = new JWTUtils();
        User user;
        response = new Response();
        if (email != null) {
            user = userJpaRepository.findByEmail(email);
            if (user == null) {
                return response.sendFailure(Status.NONE_USER, "用户不存在！");
            }
        } else {
            if (phone == null) {
                throw new NoSuchFieldException("phone字段与email字段不可同时为空！");
            } else {
                user = userJpaRepository.findByPhone(phone);
                if (user == null) {
                    return response.sendFailure(Status.NONE_USER, "用户不存在！");
                }
            }
        }
        if (user.getPassword().equals(password)) {
            userJpaRepository.updateLoginDateByUserId(System.currentTimeMillis(), user.getUserId());//更新用户登陆时间

            String token = jwtUtils.generateToken(user);//生成用户token
            log.info("token:  " + token);
            //注意：后端从数据库中查找到的数据不可以直接返回，需要重新自定义一个数据结构！
            UserBasic loginResult = new UserBasic(user);//转换返回的数据为前端需要的数据
            response.insert("token", token);
            response.insert("user", loginResult);
            response.insert("refreshToken", jwtUtils.generateRefreshToken(user));
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.WRONG_PASSWORD, "密码错误！");
        }
    }

    @Override
    public String register(String nickname, String email, String password, int gender, String phone, String headUrl) {
        //注册时间
        long time = System.currentTimeMillis();
        JWTUtils jwtUtils = new JWTUtils();
        User user = new User();
        response = new Response();
        if (userJpaRepository.existsByEmail(email)) {
            return response.sendFailure(Status.EMAIL_HAS_REGISTER, "邮箱已经使用过了哦！换个邮箱试试？");
        }
        if (userJpaRepository.existsByPhone(phone)) {
            return response.sendFailure(Status.PHONE_HAS_REGISTER, "手机号码已经使用过了哦！换个号码试试？");
        }
        if (userJpaRepository.existsByNickname(nickname)) {
            return response.sendFailure(Status.NICKNAME_EXIST, "昵称已被征用了哦，再换个试试？");
        }
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPassword(password);
        user.setGender(gender);
        user.setPhone(phone);
        user.setRegistDate(time);
        user.setHeadUrl(headUrl);
        user.setRoleId(1);
        user.setLoginDate(System.currentTimeMillis());
        userJpaRepository.save(user);
        String token = jwtUtils.generateToken(user);//生成用户token
        String refreshToken = jwtUtils.generateRefreshToken(user);
        response.insert("refreshToken", refreshToken);
        response.insert("token", token);
        response.insert("user", new UserBasic(user));
        return response.sendSuccess();
    }

    @Override
    public String sendCode(String email, String phone) throws Exception {
        // TODO 删除注释
        RedisKeyDto redisKeyDto = new RedisKeyDto();
        response = new Response();
        CodeSend codeSend = new CodeSend();
        boolean result = false;
        if (email != null) {
            String code = Code.mailCode();
            redisKeyDto.setKeys(email);
            redisKeyDto.setValues(code);
            redisService.addRedisData(redisKeyDto, EXPIRATIONTIME);
            String text = "你好，你现在正在绑定邮箱，请在 30 分钟内输入以下验证码完成绑定。 如非你本人操作，请忽略此邮件。";
            result = codeSend.mailSend(email, code,text);
        } else {
            if (phone != null) {
                String code = Code.phoneCode();
                redisKeyDto.setKeys(phone);
                redisKeyDto.setValues(code);
                redisService.addRedisData(redisKeyDto, EXPIRATIONTIME);
//                result = codeSend.phoneSend(phone, code);
                result = true;
                log.info("----------验证码是：" + code);
            } else {
                throw new NoSuchFieldException("手机和邮箱不能同时为空!");
            }
        }
        if (result) {
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.CODE_SEND_FAILURE, "验证码发送失败");
        }
    }

    @Override
    public String verifyCode(String phone, String email, String code) throws NoSuchFieldException {
        response = new Response();
        RedisKeyDto redisKeyDto = new RedisKeyDto();
        if (email == null) {
            if (phone != null) {
                redisKeyDto.setKeys(phone);
                RedisKeyDto result = redisService.redisGet(redisKeyDto);
                if (result != null && result.getValues().equals(String.valueOf(code))) {
                    redisService.delete(redisKeyDto);
                    return response.sendSuccess();
                }
                return response.sendFailure(Status.CODE_ERROR, "验证码错误");
            } else {
                throw new NoSuchFieldException("手机和邮箱不能同时为空！");
            }
        } else {
            redisKeyDto.setKeys(email);
            RedisKeyDto result = redisService.redisGet(redisKeyDto);
            if (result != null && result.getValues().equals(String.valueOf(code))) {
                redisService.delete(redisKeyDto);
                return response.sendSuccess();
            }
            return response.sendFailure(Status.CODE_ERROR, "验证码错误");
        }

    }

    @Override
    public String loginCode(String phone, String code) throws NoSuchFieldException {
        RedisKeyDto redisKeyDto = new RedisKeyDto();
        response = new Response();
        JWTUtils jwtUtils = new JWTUtils();
        User user;
        if (phone != null) {
            user = userJpaRepository.findByPhone(phone);
            if (user != null) {
                //用户存在
                redisKeyDto.setKeys(phone);
                RedisKeyDto result = redisService.redisGet(redisKeyDto);
                if (result != null && result.getValues().equals(String.valueOf(code))) {//验证成功
                    userJpaRepository.updateLoginDateByUserId(System.currentTimeMillis(), user.getUserId());//更新用户登陆时间
                    redisService.delete(redisKeyDto);
                    String token = jwtUtils.generateToken(user);
                    String refreshToken = jwtUtils.generateRefreshToken(user);
                    log.info("token:  " + token);
                    log.info("refreshToken:  " + refreshToken);
                    UserBasic loginResult = new UserBasic(user);
                    response.insert("token", token);
                    response.insert("refreshToken", refreshToken);
                    response.insert("user", loginResult);
                    return response.sendSuccess();
                } else {
                    return response.sendFailure(Status.CODE_ERROR, "验证码错误！");
                }
            } else {
                return response.sendFailure(Status.NONE_USER, "用户不存在");
            }
        } else {
            throw new NoSuchFieldException("请输入手机号！");
        }
    }

    @Override
    public String forgetPassword(String email, String phone, String password, String code) throws NoSuchFieldException {

        RedisKeyDto redisKeyDto = new RedisKeyDto();
        response = new Response();
        if (phone == null) {
            if (email != null) {
                redisKeyDto.setKeys(email);
                RedisKeyDto result = redisService.redisGet(redisKeyDto);
                if (result != null && result.getValues().equals(String.valueOf(code))) {
                    redisService.delete(redisKeyDto);
                    userJpaRepository.updatePasswordByEmail(password, email);
                    return response.sendSuccess();
                } else {
                    return response.sendFailure(Status.CODE_ERROR, "验证码错误！");
                }
            } else {
                throw new NoSuchFieldException("邮箱和手机不能同时为空！");
            }
        } else {
            redisKeyDto.setKeys(phone);
            RedisKeyDto result = redisService.redisGet(redisKeyDto);
            if (result != null && result.getValues().equals(String.valueOf(code))) {
                redisService.delete(redisKeyDto);
                userJpaRepository.updatePasswordByPhone(password, phone);
                return response.sendSuccess();
            } else {
                return response.sendFailure(Status.CODE_ERROR, "验证码错误！");
            }
        }
    }

    @Override
    public String existUser(String email, String phone, String nickname) throws NoSuchFieldException {
        response = new Response();
        boolean exist = false;
        if (email != null && userJpaRepository.existsByEmail(email)) {
            exist = true;
        } else if (phone != null && userJpaRepository.existsByPhone(phone)) {
            exist = true;
        } else if (nickname != null && userJpaRepository.existsByNickname(nickname)) {
            exist = true;
        } else if (email == null && phone == null && nickname == null) {
            throw new NoSuchFieldException();
        }
        response.insert("exist", exist);
        return response.sendSuccess();
    }
}
