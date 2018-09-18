package com.cxyzj.cxyzjback.Service.impl.User.front;

import com.cxyzj.cxyzjback.Bean.Redis.RedisKeyDto;
import com.cxyzj.cxyzjback.Bean.User.Attention;
import com.cxyzj.cxyzjback.Bean.User.User;
import com.cxyzj.cxyzjback.Data.User.OtherDetails;
import com.cxyzj.cxyzjback.Data.User.OtherSimple;
import com.cxyzj.cxyzjback.Data.User.UserDetails;
import com.cxyzj.cxyzjback.Data.User.UserSimple;
import com.cxyzj.cxyzjback.Repository.User.UserAttentionJpaRepository;
import com.cxyzj.cxyzjback.Repository.User.UserJpaRepository;
import com.cxyzj.cxyzjback.Service.Interface.Other.RedisService;
import com.cxyzj.cxyzjback.Service.Interface.User.front.UserInfoService;
import com.cxyzj.cxyzjback.Utils.Code;
import com.cxyzj.cxyzjback.Utils.CodeSend;
import com.cxyzj.cxyzjback.Utils.JWT.JWTUtils;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @Author 夏
 * @Date 10:15 2018/8/25
 */

@Service
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserAttentionJpaRepository userAttentionJpaRepository;
    private RedisKeyDto redisKeyDto;
    private Response response;
    private String userId;
    private User user;


    //短信验证码过期时间 单位：seconds
    private  static int  EXPIRATIONTIME=60;

    //修改关键信息的缓存时间
    private static int ALLOWCHANGE = 60*5;

    @Resource
    private RedisService redisService;


    /**
     * @Description 获取用户（自己）详细信息（ROLE_USER）
     * @return 用户（详细）信息
     */
    @Override
    public String detailsOwn() {
        response = new Response();
        user = userJpaRepository.findByUserId(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user != null) {
            response.insert(new UserDetails(user));
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.NONE_USER,"用户不存在");
        }
    }

    /**
     * @Description 获取用户（自己）简要信息（ROLE_USER）
     * @return 用户（简要）信息
     */
    @Override
    public String simpleOwn() {
        response = new Response();
        user = userJpaRepository.findByUserId(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user != null) {
            response.insert(new UserSimple(user));
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.NONE_USER,"用户不存在");
        }
    }

    /**
     * @Description 获取其他用户详细信息（ROLE_ANONYMITY）
     * @param otherId （其他）用户id
     * @return 其他用户（详细）信息
     */
    @Override
    public String detailsOther(String otherId) {

        response = new Response();
        OtherDetails userOther = new OtherDetails(userJpaRepository.findByUserId(otherId));

        if(userJpaRepository.existsByUserId(otherId)){
            userId = SecurityContextHolder.getContext().getAuthentication().getName();
            if(userAttentionJpaRepository.existsByUserIdAndTargetUser(userId, otherId)){
                //根据自己（userId）和目标用户（targetId）查询关系表，如果关系存在，查询status，如果status=201：设置is_followed=true，
                // 如果status=203: 互相关注，也设置is_followed=true
                if(userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, otherId) == 201||
                        userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, otherId) == 203){
                    userOther.set_followed(true);
                    response.insert(userOther);
                }else{
                    //其他默认为is_followed=false
                    response.insert(userOther);
                }
            }else{
                //如果关系不存在，默认关系为is_followed=false
                response.insert(userOther);
            }
            return response.sendSuccess();
        }else {
            return response.sendFailure(Status.NONE_USER,"用户不存在");
        }
    }

    /**
     * @Description 修改用户昵称（ROLE_USER）
     * @param nickname 昵称
     * @return success||failure
     */
    @Override
    public String updateNickname(String nickname) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!userJpaRepository.existsByNickname(nickname)){
            userJpaRepository.updateNicknameByUserId(nickname, userId);
            user = userJpaRepository.findByUserId(userId);
            response.insert("nickname",user.getNickname());
            return response.sendSuccess();
        }else{
            return response.sendFailure(Status.NICKNAME_EXIST,"昵称已存在");
        }
    }

    /**
     * @Description 修改用户头像（ROLE_USER）
     * @param headUrl 头像路径
     * @return success
     */
    @Override
    public String updateHead(String headUrl) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        userJpaRepository.updateHeadByUserId(headUrl,userId);
        user = userJpaRepository.findByUserId(userId);

        response.insert("head_url",user.getHeadUrl());
        return response.sendSuccess();
    }

    /**
     * @Description 修改用户性别（ROLE_USER）
     * @param gender 性别
     * @return success
     */
    @Override
    public String updateGender(String gender) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        userJpaRepository.updateGenderByUserId(gender, userId);
        user = userJpaRepository.findByUserId(userId);

        response.insert("gender", gender);
        return response.sendSuccess();
    }

    /**
     * @Description 修改个人介绍（ROLE_USER）
     * @param introduce 介绍
     * @return success
     */
    @Override
    public String updateIntroduce(String introduce) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        userJpaRepository.updateIntroduceByUserId(introduce, userId);
        user = userJpaRepository.findByUserId(userId);

        response.insert("introduce", introduce);
        return response.sendSuccess();
    }

    /**
     * @Description 修改主题颜色（ROLE_USER）
     * @param themeColor 主题颜色
     * @return success
     */
    @Override
    public String updateThemeColor(String themeColor) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        userJpaRepository.updateThemeColorByUserId(themeColor, userId);
        user = userJpaRepository.findByUserId(userId);

        response.insert("theme_color",user.getThemeColor());
        return response.sendSuccess();
    }

    /**
     * @Description 修改背景图片（ROLE_USER）
     * @param bgUrl 图片路径
     * @return success
     */
    @Override
    public String updateBg(String bgUrl) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        userJpaRepository.updateBgUrlByUserId(bgUrl, userId);
        user = userJpaRepository.findByUserId(userId);

        response.insert("bg_url",bgUrl);
        return response.sendSuccess();
    }

    /**
     * @Description 发送验证码（ROLE_USER）
     * @param verifyType 验证方式（email || phone）
     * @return success
     */
    @Override
    public String sendCode(String verifyType) {
        redisKeyDto=new RedisKeyDto();
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        user = userJpaRepository.findByUserId(userId);
        CodeSend codeSend = new CodeSend();
        if(verifyType.equals("email")){
            String email = user.getEmail();
            String code = new Code().mailCode();
            //将userId作为key，code作为Values缓存进redis
            redisKeyDto.setKeys(userId);
            redisKeyDto.setValues(code);
            redisService.addRedisData(redisKeyDto,EXPIRATIONTIME);
            return codeSend.mailSend(email, code);
        }else if(verifyType.equals("phone")){
            String phone = user.getPhone();
            String code = new Code().phoneCode();
            //将userId作为key，code作为Values缓存进redis
            redisKeyDto.setKeys(userId);
            redisKeyDto.setValues(code);
            redisService.addRedisData(redisKeyDto,EXPIRATIONTIME);
            try {
                return codeSend.phoneSend(phone, code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            return response.sendFailure(Status.CODE_SEND_FAILURE,"验证码发送失败");
        }
        return verifyType;
    }

    /**
     * @Description 验证码校验（ROLE_USER）
     * @param code 验证码
     * @return success || failure
     */
    @Override
    public String verify(String code) {
        redisKeyDto=new RedisKeyDto();
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();

        //已userId作为key，来查询redis缓存中的value值
        redisKeyDto.setKeys(userId);
        RedisKeyDto result = redisService.redisGet(redisKeyDto);

        //判断值是否正确
        if(result != null&&result.getValues().equals(String.valueOf(code))) {

            //如果验证码正确，则先将之前存在redis中的键值对删除，重新添加一个键值对，主键仍为id，value值为“allowChange”
            redisService.delete(redisKeyDto);
            redisKeyDto.setKeys(userId);
            redisKeyDto.setValues("allowChange");
            redisService.addRedisData(redisKeyDto, ALLOWCHANGE);
            return response.sendSuccess();
        }
        return response.sendFailure(Status.CODE_ERROR,"验证码错误");
    }

    /**
     * @Description 修改密码（ROLE_USER），在验证成功之后
     * @param password 密码
     * @param user_id  用户id
     * @return success || failure
     */
    @Override
    public String updatePassword(String password, String user_id) {
        redisKeyDto = new RedisKeyDto();
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();

        redisKeyDto.setKeys(userId);
        RedisKeyDto result = redisService.redisGet(redisKeyDto);
        if(result != null && result.getValues().equals("allowChange")){
            userJpaRepository.updatePasswordByUserId(password, user_id);
            return response.sendSuccess();
        }
        return response.sendFailure(Status.OUT_OF_TIME,"验证码已过期，请重新验证！");
    }

    /**
     * @Description 修改手机号码（ROLE_USER），在验证成功之后
     * @param phone 手机号码
     * @param user_id  用户id
     * @return success || failure
     */
    @Override
    public String updatePhone(String phone, String user_id) {
        redisKeyDto = new RedisKeyDto();
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();

        redisKeyDto.setKeys(userId);
        RedisKeyDto result = redisService.redisGet(redisKeyDto);
        if(result != null && result.getValues().equals("allowChange")){
            userJpaRepository.updatePhoneByUserId(phone, user_id);
            return response.sendSuccess();
        }
        return response.sendFailure(Status.OUT_OF_TIME,"验证码已过期，请重新验证！");
    }

    /**
     * @Description 修改邮箱（ROLE_USER），在验证成功之后
     * @param email 邮箱
     * @param user_id  用户id
     * @return success || failure
     */
    @Override
    public String updateEmail(String email, String user_id) {
        redisKeyDto = new RedisKeyDto();
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();

        redisKeyDto.setKeys(userId);
        RedisKeyDto result = redisService.redisGet(redisKeyDto);
        if(result != null && result.getValues().equals("allowChange")){
            userJpaRepository.updateEmailByUserId(email, user_id);
            return response.sendSuccess();
        }
        return response.sendFailure(Status.OUT_OF_TIME,"验证码已过期，请重新验证！");
    }

    /**
     * @Description 刷新token（ROLE_USER）
     * @return token
     */
    @Override
    public String refreshToken() {
        JWTUtils jwtUtils = new JWTUtils();
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userJpaRepository.findByUserId(userId);
        String token = jwtUtils.generateRefreshToken(user);

        response.insert("token", token);
        return response.sendSuccess();
    }

    /**
     * @Description 获取其他用户简要信息（ROLE_ANONYMITY）
     * @param otherId （其他）用户id
     * @return 其他用户（详细）信息
     */
    @Override
    public String simpleOther(String otherId) {
        response = new Response();
        OtherSimple userOther = new OtherSimple(userJpaRepository.findByUserId(otherId));

        if(userJpaRepository.existsByUserId(otherId)){
            userId = SecurityContextHolder.getContext().getAuthentication().getName();
            if(userAttentionJpaRepository.existsByUserIdAndTargetUser(userId, otherId)){
                if(userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, otherId) == 201||
                        userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, otherId) == 203){
                    userOther.set_followed(true);
                    response.insert("user",userOther);
                }else{
                    response.insert("user",userOther);
                }
            }else{
                response.insert("user",userOther);
            }
            return response.sendSuccess();
        }else {
            return response.sendFailure(Status.NONE_USER,"用户不存在");
        }
    }

    /**
     * @Description 关注用户（ROLE_USER）
     * @param targetId （目标）用户id
     * @return
     */
    @Override
    public String follow(String targetId) {
        user = new User();
        response = new Response();
        Attention attention;
        userId = SecurityContextHolder.getContext().getAuthentication().getName();//读取token中的用户id
        //检查attention表中，是否有存在目标用户的关系
        if(!userAttentionJpaRepository.existsByUserIdAndTargetUser(userId, targetId)
                && !userAttentionJpaRepository.existsByUserIdAndTargetUser(targetId, userId)){
            //如果没有，则创建 201 和202的关系
            attention = new Attention();
            attention.setUserId(userId);
            attention.setTargetUser(targetId);
            attention.setStatus(201);
            userAttentionJpaRepository.save(attention);

            Attention attention1 = new Attention();
            attention1.setUserId(targetId);
            attention1.setTargetUser(userId);
            attention1.setStatus(202);
            userAttentionJpaRepository.save(attention1);
            userJpaRepository.updateFansByUserId(userJpaRepository.getUserFans(targetId) + 1, targetId);
            userAttentionJpaRepository.updateAttentionsByUserId(userAttentionJpaRepository.getUserAttentions(userId) + 1, userId);
            response.insert("fans", userJpaRepository.getUserFans(targetId));
            return response.sendSuccess();
        }else if(!userAttentionJpaRepository.existsByUserIdAndTargetUser(targetId, userId)&&
                userAttentionJpaRepository.findByUserIdAndTargetUser(targetId, userId).getStatus() != 202 &&
                userAttentionJpaRepository.findByUserIdAndTargetUser(targetId, userId).getStatus() != 203){
            //如果有，但不是互相关注的状态，删除之前的201和202关系，创建203（互相关注）关系
            userAttentionJpaRepository.deleteByUserId(userId);
            userAttentionJpaRepository.deleteByUserId(targetId);
            attention = new Attention();
            attention.setUserId(userId);
            attention.setTargetUser(targetId);
            attention.setStatus(203);
            userAttentionJpaRepository.save(attention);

            Attention attention1 = new Attention();
            attention1.setUserId(targetId);
            attention1.setTargetUser(userId);
            attention1.setStatus(203);
            userAttentionJpaRepository.save(attention1);

            userJpaRepository.updateFansByUserId(userJpaRepository.getUserFans(targetId) + 1, targetId);
            userAttentionJpaRepository.updateAttentionsByUserId(userAttentionJpaRepository.getUserAttentions(userId) + 1, userId);
            response.insert("fans", userJpaRepository.getUserFans(targetId));
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.USER_HAS_FOLLOWED, "该用户您已关注");
        }
    }

    /**
     * @Description 取消关注（ROLE_USER）
     * @param targetId （目标）用户id
     * @return
     */
    @Override
    public String delFollow(String targetId) {
        user = new User();
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();//读取token中的用户id
        //判断关系是否存在
        if(userAttentionJpaRepository.existsByUserIdAndTargetUser(userId, targetId)
                && userAttentionJpaRepository.findByUserIdAndTargetUser(targetId, userId).getStatus() == 202){
            //如果存在且不是互相关注的状态，则删除attention中的关系数据
            userAttentionJpaRepository.deleteByUserIdAndTargetUser(userId, targetId);
            userAttentionJpaRepository.deleteByUserIdAndTargetUser(targetId, userId);
            userJpaRepository.updateFansByUserId(userJpaRepository.getUserFans(targetId) - 1, targetId);
            userAttentionJpaRepository.updateAttentionsByUserId(userAttentionJpaRepository.getUserAttentions(userId) - 1, userId);
            response.insert("fans", userJpaRepository.getUserFans(targetId));
            return response.sendSuccess();
        }else if(userAttentionJpaRepository.existsByUserIdAndTargetUser(userId, targetId)
                && userAttentionJpaRepository.findByUserIdAndTargetUser(targetId, userId).getStatus() == 203) {
            //如果存在且是互相关注的状态，将更新attention表中的关系为非互相关注（被关注）的状态
            userAttentionJpaRepository.updateStatusByUserAndTargetUser(201, targetId, userId);
            userAttentionJpaRepository.updateStatusByUserAndTargetUser(202, userId, targetId);
            userJpaRepository.updateFansByUserId(userJpaRepository.getUserFans(targetId) - 1, targetId);
            userAttentionJpaRepository.updateAttentionsByUserId(userAttentionJpaRepository.getUserAttentions(userId) - 1, userId);
            response.insert("fans", userJpaRepository.getUserFans(targetId));
            return response.sendSuccess();
        }else {
            return response.sendFailure(Status.USER_NOT_FOLLOWED,"该用户未关注");
        }
    }
}
