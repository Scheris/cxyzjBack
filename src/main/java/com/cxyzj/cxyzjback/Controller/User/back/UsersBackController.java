package com.cxyzj.cxyzjback.Controller.User.back;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1/back/users")
public class UsersBackController {

    @PatchMapping("/alter_is_admin")
    @PreAuthorize("hasAnyRole('ROLE_ADMINISTRATORS')")
    public String alertIsAdmin(@RequestParam String access_token){
        return null;
    }

}
