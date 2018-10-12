package com.cxyzj.cxyzjback.Service.impl.User.front;

import com.cxyzj.cxyzjback.Bean.Redis.RedisKeyDto;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.User.UserBasic;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Other.RedisService;
import com.cxyzj.cxyzjback.Service.Interface.User.front.AuthService;
import com.cxyzj.cxyzjback.Utils.*;
import com.cxyzj.cxyzjback.Utils.JWT.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @Package com.cxyzj.cxyzjback.Service.impl
 * @Author Yaser
 * @Date 2018/08/10 15:38
 * @Description: 用户登录注册API
 * @checked true
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserJpaRepository userJpaRepository;
    private Response response;
    private final Utils utils;
    //短信验证码过期时间,10分钟
    private static final int EXPIRATION_TIME = 60 * 1000 * 10;

    @Resource
    private RedisService redisService;

    @Autowired
    public AuthServiceImpl(UserJpaRepository userJpaRepository, Utils utils) {
        this.userJpaRepository = userJpaRepository;
        this.utils = utils;
    }

    /**
     * @param email    邮箱
     * @param phone    手机
     * @param password 密码
     * @return 用户信息
     * @throws NoSuchFieldException 邮箱或手机均为空
     * @checked true
     */
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
            UserBasic loginResult = new UserBasic(user);
            response.insert("user", loginResult);
            response.insert("token", jwtUtils.generateToken(user));
            response.insert("refreshToken", jwtUtils.generateRefreshToken(user));
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.WRONG_PASSWORD, "密码错误！");
        }
    }

    /**
     * @param nickname 昵称
     * @param email    邮箱
     * @param password 密码
     * @param gender   性别
     * @param phone    手机
     * @param headUrl  头像
     * @return 用户信息
     * @checked true
     */
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
        user.setLoginDate(System.currentTimeMillis());
        userJpaRepository.save(user);
        String token = jwtUtils.generateToken(user);//生成用户token
        String refreshToken = jwtUtils.generateRefreshToken(user);//生成用户refreshToken
        response.insert("refreshToken", refreshToken);
        response.insert("token", token);
        response.insert("user", new UserBasic(user));
        return response.sendSuccess();
    }

    /**
     * @param email 邮箱
     * @param phone 手机
     * @return 验证码是否发送成功
     * @throws NoSuchFieldException 手机邮箱均为空
     * @checked true
     * @Description: 项目完成后需要将手机发送的注释去掉
     */
    @Override
    public String sendCode(String email, String phone) throws NoSuchFieldException {
        // TODO 删除注释
        RedisKeyDto redisKeyDto = new RedisKeyDto();
        response = new Response();
        boolean result;
        if (email != null) {
            String code = utils.mailCode();
            redisKeyDto.setKeys(email);
            redisKeyDto.setValues(code);
            redisService.addRedisData(redisKeyDto, EXPIRATION_TIME);
            String text = "你好，你现在正在绑定邮箱，请在 30 分钟内输入以下验证码完成绑定。 如非你本人操作，请忽略此邮件。";
            result = utils.mailSend(email, code, text);
        } else {
            if (phone != null) {
                String code = utils.phoneCode();
                redisKeyDto.setKeys(phone);
                redisKeyDto.setValues(code);
                redisService.addRedisData(redisKeyDto, EXPIRATION_TIME);
//                result = utils.phoneSend(phone, code);
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

    /**
     * @param phone 手机
     * @param email 邮箱
     * @param code  验证码
     * @return 是否验证成功
     * @throws NoSuchFieldException 邮箱手机均为空
     * @checked true
     */
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

    /**
     * @param phone 手机
     * @param code  验证码
     * @return 用户信息
     * @checked true
     */

    @Override
    public String loginCode(String phone, String code) {
        RedisKeyDto redisKeyDto = new RedisKeyDto();
        response = new Response();
        JWTUtils jwtUtils = new JWTUtils();
        User user;
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
    }

    /**
     * @param email    邮箱
     * @param phone    手机
     * @param password 密码
     * @param code     验证码
     * @return 密码是否重置成功
     * @throws NoSuchFieldException 邮箱或手机均为空
     * @checked true
     */
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

    /**
     * @param email    邮箱
     * @param phone    手机
     * @param nickname 昵称
     * @return 用户是否存在
     * @throws NoSuchFieldException 参数均为空
     * @checked true
     */
    @Override
    public String existUser(String email, String phone, String nickname) throws NoSuchFieldException {
        response = new Response();
        boolean exist;
        if (email == null && phone == null && nickname == null) {
            throw new NoSuchFieldException();
        } else {
            exist = userJpaRepository.existsByEmailOrPhoneOrNickname(email, phone, nickname);
        }
        response.insert("exist", exist);
        return response.sendSuccess();
    }
}
