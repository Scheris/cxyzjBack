package com.cxyzj.cxyzjback.Controller.User;


import com.cxyzj.cxyzjback.Service.Interface.User.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Author 夏
 * @Date 09:02 2018/8/21
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1/user")
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;

    @GetMapping(value = "/details/own")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getUserDetail() {
        return userInfoService.detailsOwn();
    }

    @GetMapping(value = "/simple/own")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String getUserSimple() {
        return userInfoService.simpleOwn();
    }

    @GetMapping(value = "/details/other/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS') and principal.username.equals(#userId)")
    public String getOtherDetail(@PathVariable(name = "userId") String userId) {
        return userInfoService.detailsOther(userId);
    }

    @PostMapping(value = "/update_nickname")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String updateNickname(@RequestParam String nickname){
        return userInfoService.updateNickname(nickname);
    }

    @PostMapping(value = "/update_head")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String updateHead(@RequestParam String head_url){
        return userInfoService.updateHead(head_url);
    }

    @PostMapping(value = "/update_gender")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String updateGender(@RequestParam String gender){
        return userInfoService.updateGender(gender);
    }

    @PostMapping(value = "/update_introduce")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String updateIntroduce(@RequestParam String introduce){
        return userInfoService.updateIntroduce(introduce);
    }

    @PostMapping(value = "/update_theme_color")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String updateThemeColor(@RequestParam String theme_color){
        return userInfoService.updateThemeColor(theme_color);
    }

    @PostMapping(value = "/update_bg")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String updateBg(@RequestParam String bg_url){
        return userInfoService.updateBg(bg_url);
    }

    @PostMapping(value = "/send_code")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String sendCode(@RequestParam String verify_type) {
        return userInfoService.sendCode(verify_type);
    }

    @PostMapping(value = "/verify")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String verify(@RequestParam String code) {
        return userInfoService.verify(code);
    }

    @PostMapping(value = "/update_password")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String updatePassword(@RequestParam String password, @RequestParam String user_id){
        return userInfoService.updatePassword(password, user_id);
    }

    @PostMapping(value = "/update_phone")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String updatePhone(@RequestParam String phone, @RequestParam String user_id){
        return userInfoService.updatePhone(phone, user_id);
    }

    @PostMapping(value = "/update_email")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String updateEmail(@RequestParam String email, @RequestParam String user_id){
        return userInfoService.updateEmail(email, user_id);
    }

    @GetMapping(value = "/refresh_token")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String refreshToken(){
        return userInfoService.refreshToken();
    }

    @GetMapping(value = "/simple/other/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS') and principal.username.equals(#userId)")
    public String getOtherSimple(@PathVariable(name = "userId") String userId) {
        return userInfoService.simpleOther(userId);
    }

    @PutMapping(value = "/follow/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String follow(@PathVariable(name = "userId") String targetId){
        return userInfoService.follow(targetId);
    }

//    @DeleteMapping(value = "/follow/{userId}")
//    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
//    public String delFollow(@PathVariable(name = "userId") String targetId) {
//        return userInfoService.delFollow(targetId);
//    }

}
