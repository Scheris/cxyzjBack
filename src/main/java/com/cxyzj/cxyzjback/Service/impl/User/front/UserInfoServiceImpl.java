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
import com.cxyzj.cxyzjback.Utils.*;
import com.cxyzj.cxyzjback.Utils.JWT.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @Author 夏
 * @Date 10:15 Status.FOCUS8/8/25
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
    private static final int EXPIRATION_TIME = 60;

    //修改关键信息的缓存时间
    private static final int ALLOW_CHANGE = 5*EXPIRATION_TIME;

    @Resource
    private RedisService redisService;


    /**
     * @return 用户（详细）信息
     * @Description 获取用户（自己）详细信息（ROLE_USER）
     * @checked true
     */
    @Override
    public String detailsOwn() {
        response = new Response();
        user = userJpaRepository.findByUserId(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user != null) {
            response.insert(new UserDetails(user));
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.NONE_USER, "用户不存在");
        }
    }

    /**
     * @return 用户（简要）信息
     * @Description 获取用户（自己）简要信息（ROLE_USER）
     * @checked true
     */
    @Override
    public String simpleOwn() {
        response = new Response();
        user = userJpaRepository.findByUserId(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user != null) {
            response.insert(new UserSimple(user));
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.NONE_USER, "用户不存在");
        }
    }

    /**
     * @param otherId （其他）用户id
     * @return 其他用户（详细）信息
     * @Description 获取其他用户详细信息（ROLE_ANONYMITY）
     * @checked true
     */
    @Override
    public String detailsOther(String otherId) {
        response = new Response();
        if (userJpaRepository.existsByUserId(otherId)) {
            OtherDetails userOther = new OtherDetails(userJpaRepository.findByUserId(otherId));
            userId = SecurityContextHolder.getContext().getAuthentication().getName();
            if (userAttentionJpaRepository.existsByUserIdAndTargetUser(userId, otherId)) {
                int relation = userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, otherId);
                log.info(String.valueOf(relation));
                //根据自己（userId）和目标用户（targetId）查询关系表，如果关系存在，查询status，如果status=Status.FOCUS：设置is_followed=true，
                // 如果status=203: 互相关注，也设置is_followed=true
                if (relation == Constant.FOCUS || relation == Constant.EACH) {
                    userOther.set_followed(true);
                }
            }
            response.insert(userOther);
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.NONE_USER, "用户不存在");
        }
    }

    /**
     * @param nickname 昵称
     * @return success||failure
     * @Description 修改用户昵称（ROLE_USER）
     * @checked true
     */
    @Override
    public String updateNickname(String nickname) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!userJpaRepository.existsByNickname(nickname)) {
            userJpaRepository.updateNicknameByUserId(nickname, userId);
            response.insert("nickname", nickname);
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.NICKNAME_EXIST, "昵称已存在");
        }
    }

    /**
     * @param headUrl 头像路径
     * @return success
     * @Description 修改用户头像（ROLE_USER）
     * @checked true
     */
    @Override
    public String updateHead(String headUrl) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        userJpaRepository.updateHeadByUserId(headUrl, userId);
        response.insert("head_url", headUrl);
        return response.sendSuccess();
    }

    /**
     * @param gender 性别
     * @return success
     * @Description 修改用户性别（ROLE_USER）
     * @checked true
     */
    @Override
    public String updateGender(String gender) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        userJpaRepository.updateGenderByUserId(gender, userId);
        response.insert("gender", gender);
        return response.sendSuccess();
    }

    /**
     * @param introduce 介绍
     * @return success
     * @Description 修改个人介绍（ROLE_USER）
     * @checked true
     */
    @Override
    public String updateIntroduce(String introduce) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        userJpaRepository.updateIntroduceByUserId(introduce, userId);
        response.insert("introduce", introduce);
        return response.sendSuccess();
    }

    /**
     * @param themeColor 主题颜色
     * @return success
     * @Description 修改主题颜色（ROLE_USER）
     * @checked true
     */
    @Override
    public String updateThemeColor(String themeColor) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        userJpaRepository.updateThemeColorByUserId(themeColor, userId);
        response.insert("theme_color", themeColor);
        return response.sendSuccess();
    }

    /**
     * @param bgUrl 图片路径
     * @return success
     * @Description 修改背景图片（ROLE_USER）
     * @checked true
     */
    @Override
    public String updateBg(String bgUrl) {
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        userJpaRepository.updateBgUrlByUserId(bgUrl, userId);
        response.insert("bg_url", bgUrl);
        return response.sendSuccess();
    }

    /**
     * @param verifyType 验证方式（email || phone）
     * @return success
     * @Description 发送验证码（ROLE_USER）
     * @checked true
     */
    @Override
    public String sendCode(String verifyType) {
        //TODO 将发送代码的逻辑取消注释
        redisKeyDto = new RedisKeyDto();
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        user = userJpaRepository.findByUserId(userId);
        if (user == null) {
            return response.sendFailure(Status.ILLEGAL_USER, "非法的用户！");
        }
        CodeSend codeSend = new CodeSend();
        boolean result = false;
        String code;
        switch (verifyType) {
            case "email":
                String email = user.getEmail();
                code = Code.mailCode();
                //将userId作为key，code作为Values缓存进redis
                redisKeyDto.setKeys(userId);
                redisKeyDto.setValues(code);
                redisService.addRedisData(redisKeyDto, EXPIRATION_TIME);
                String text = user.getNickname()+",你好，\n你正在验证绑定的邮箱，请在 30 分钟内输入以下验证码完成验证。如非你本人操作，请忽略此邮件。\n你的邮箱验证码是：";
                result = codeSend.mailSend(email, code,text);
                break;
            case "phone":
                String phone = user.getPhone();
                code = Code.phoneCode();
                //将userId作为key，code作为Values缓存进redis
                redisKeyDto.setKeys(userId);
                redisKeyDto.setValues(code);
                redisService.addRedisData(redisKeyDto, EXPIRATION_TIME);
                log.info(code);
                try {
//                    result = codeSend.phoneSend(phone, code);
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                return response.sendFailure(Status.ILLEGAL_ARGUMENT, "非法的请求参数");
        }
        if (result) {
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.CODE_SEND_FAILURE, "验证码发送失败");
        }
    }

    /**
     * @param code 验证码
     * @return success || failure
     * @Description 验证码校验（ROLE_USER）
     * @checked true
     */
    @Override
    public String verify(String code) {
        redisKeyDto = new RedisKeyDto();
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        //已userId作为key，来查询redis缓存中的value值
        redisKeyDto.setKeys(userId);
        RedisKeyDto result = redisService.redisGet(redisKeyDto);
        //判断值是否正确
        if (result != null && result.getValues().equals(String.valueOf(code))) {
            //如果验证码正确，则先将之前存在redis中的键值对删除，重新添加一个键值对，主键仍为id，value值为“allowChange”
            redisService.delete(redisKeyDto);
            redisKeyDto.setKeys(userId);
            redisKeyDto.setValues("allowChange");
            redisService.addRedisData(redisKeyDto, ALLOW_CHANGE);
            return response.sendSuccess();
        }
        return response.sendFailure(Status.CODE_ERROR, "验证码错误");
    }

    /**
     * @param password 密码
     * @param user_id  用户id
     * @return success || failure
     * @Description 修改密码（ROLE_USER），在验证成功之后
     * @checked true
     */
    @Override
    public String updatePassword(String password, String user_id) {
        redisKeyDto = new RedisKeyDto();
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        redisKeyDto.setKeys(userId);
        RedisKeyDto result = redisService.redisGet(redisKeyDto);
        if (result != null && result.getValues().equals("allowChange")) {
            userJpaRepository.updatePasswordByUserId(password, user_id);
            return response.sendSuccess();
        }
        return response.sendFailure(Status.OUT_OF_TIME, "验证码已过期，请重新验证！");
    }

    /**
     * @param phone   手机号码
     * @param user_id 用户id
     * @return success || failure
     * @Description 修改手机号码（ROLE_USER），在验证成功之后
     * @checked true
     */
    @Override
    public String updatePhone(String phone, String user_id) {
        redisKeyDto = new RedisKeyDto();
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        redisKeyDto.setKeys(userId);
        RedisKeyDto result = redisService.redisGet(redisKeyDto);
        if (result != null && result.getValues().equals("allowChange")) {
            userJpaRepository.updatePhoneByUserId(phone, user_id);
            phone = new Utils().maskEmailPhone(phone, true);
            response.insert("phone", phone);
            return response.sendSuccess();
        }
        return response.sendFailure(Status.OUT_OF_TIME, "验证码已过期，请重新验证！");
    }

    /**
     * @param email   邮箱
     * @param user_id 用户id
     * @return success || failure
     * @Description 修改邮箱（ROLE_USER），在验证成功之后
     * @checked true
     */
    @Override
    public String updateEmail(String email, String user_id) {
        redisKeyDto = new RedisKeyDto();
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        redisKeyDto.setKeys(userId);
        RedisKeyDto result = redisService.redisGet(redisKeyDto);
        if (result != null && result.getValues().equals("allowChange")) {
            userJpaRepository.updateEmailByUserId(email, user_id);
            email = new Utils().maskEmailPhone(email, false);
            response.insert("email", email);
            return response.sendSuccess();
        }
        return response.sendFailure(Status.OUT_OF_TIME, "验证码已过期，请重新验证！");
    }

    /**
     * @return token
     * @Description 刷新token（ROLE_USER）
     * @checked true
     */
    @Override
    public String refreshToken() {
        JWTUtils jwtUtils = new JWTUtils();
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userJpaRepository.findByUserId(userId);
        String token = jwtUtils.generateToken(user);
        response.insert("token", token);
        return response.sendSuccess();
    }

    /**
     * @param otherId （其他）用户id
     * @return 其他用户（详细）信息
     * @Description 获取其他用户简要信息（ROLE_ANONYMITY）
     * @checked true
     */
    @Override
    public String simpleOther(String otherId) {
        response = new Response();
        if (userJpaRepository.existsByUserId(otherId)) {
            OtherSimple userOther = new OtherSimple(userJpaRepository.findByUserId(otherId));
            userId = SecurityContextHolder.getContext().getAuthentication().getName();
            if (userAttentionJpaRepository.existsByUserIdAndTargetUser(userId, otherId)) {
                int status = userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, otherId);
                if (status == Constant.FOCUS || status == Constant.EACH) {
                    userOther.set_followed(true);
                }
            }
            response.insert("user", userOther);
            return response.sendSuccess();
        } else {
            return response.sendFailure(Status.NONE_USER, "用户不存在");
        }
    }

    /**
     * @param targetId （目标）用户id
     * @return
     * @Description 关注用户（ROLE_USER）
     * @checked true
     */
    @Override
    public String follow(String targetId) {
        user = new User();
        response = new Response();
        Attention attention;
        userId = SecurityContextHolder.getContext().getAuthentication().getName();//读取token中的用户id
        //检查attention表中，是否有存在目标用户的关系
        //约定：如果attention表中存在targetId的目标数据，则一定会有两条相应的记录，一条targetId在target_user字段上，另一条在user_id字段上
        if (!userAttentionJpaRepository.existsByUserIdAndTargetUser(userId, targetId)) {
            //如果没有，则创建关注和被关注的关系
            attention = new Attention();
            attention.setUserId(userId);
            attention.setTargetUser(targetId);
            attention.setStatus(Constant.FOCUS);//关注
            userAttentionJpaRepository.save(attention);

            Attention attention1 = new Attention();
            attention1.setUserId(targetId);
            attention1.setTargetUser(userId);
            attention1.setStatus(Constant.FOLLOWED);//被关注
            userAttentionJpaRepository.save(attention1);
            int fans = userJpaRepository.getUserFans(targetId) + 1;
            userJpaRepository.updateFansByUserId(fans, targetId);//被关注者粉丝+1
            userJpaRepository.updateAttentionsByUserId(userJpaRepository.getUserAttentions(userId) + 1, userId);//关注者关注人数+1
            response.insert("fans", fans);//返回被关注者最新的粉丝数
            return response.sendSuccess();
        } else {
            //如果有，先判断status是不是处于focus状态
            int status = userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, targetId);
            if (status == Constant.FOCUS) {
                //表示已经关注过了
                return response.sendFailure(Status.USER_HAS_FOLLOWED, "该用户您已关注");
            } else {
                // 但不是互相关注的状态，删除之前的关注和被关注的关系，创建互相关注关系
                userAttentionJpaRepository.deleteByUserId(userId);
                userAttentionJpaRepository.deleteByUserId(targetId);
                attention = new Attention();
                attention.setUserId(userId);
                attention.setTargetUser(targetId);
                attention.setStatus(Constant.EACH);
                userAttentionJpaRepository.save(attention);

                Attention attention1 = new Attention();
                attention1.setUserId(targetId);
                attention1.setTargetUser(userId);
                attention1.setStatus(Constant.EACH);
                userAttentionJpaRepository.save(attention1);

                userJpaRepository.updateFansByUserId(userJpaRepository.getUserFans(targetId) + 1, targetId);
                userJpaRepository.updateAttentionsByUserId(userJpaRepository.getUserAttentions(userId) + 1, userId);
                response.insert("fans", userJpaRepository.getUserFans(targetId));
                return response.sendSuccess();
            }
        }
    }

    /**
     * @param targetId （目标）用户id
     * @return
     * @Description 取消关注（ROLE_USER）
     * @checked true
     */
    @Override
    public String delFollow(String targetId) {
        user = new User();
        response = new Response();
        userId = SecurityContextHolder.getContext().getAuthentication().getName();//读取token中的用户id
        //判断关系是否存在
        if (userAttentionJpaRepository.existsByUserIdAndTargetUser(userId, targetId)) {
            //存在关系
            int status = userAttentionJpaRepository.findStatusByUserIdAndTargetUser(userId, targetId);
            if (status == Constant.FOCUS) {
                //如果是关注状态，则删除记录
                userAttentionJpaRepository.deleteByUserIdAndTargetUser(userId, targetId);
                userAttentionJpaRepository.deleteByUserIdAndTargetUser(targetId, userId);
                int fans = userJpaRepository.getUserFans(targetId) - 1;
                userJpaRepository.updateFansByUserId(fans, targetId);
                userJpaRepository.updateAttentionsByUserId(userJpaRepository.getUserAttentions(userId) - 1, userId);
                response.insert("fans", fans);
                return response.sendSuccess();
            } else if (status == Constant.EACH) {
                //如果是互相关注状态，则更新状态为user被target关注
                userAttentionJpaRepository.updateStatusByUserAndTargetUser(Constant.FOCUS, targetId, userId);
                userAttentionJpaRepository.updateStatusByUserAndTargetUser(Constant.FOLLOWED, userId, targetId);
                int fans = userJpaRepository.getUserFans(targetId) - 1;
                userJpaRepository.updateFansByUserId(fans, targetId);
                userJpaRepository.updateAttentionsByUserId(userJpaRepository.getUserAttentions(userId) - 1, userId);
                response.insert("fans", fans);
                return response.sendSuccess();
            } else {
                //如果是被关注状态
                return response.sendFailure(Status.USER_NOT_FOLLOWED, "该用户未关注");
            }
        } else {
            //不存在关系
            return response.sendFailure(Status.USER_NOT_FOLLOWED, "该用户未关注");
        }
    }
}
