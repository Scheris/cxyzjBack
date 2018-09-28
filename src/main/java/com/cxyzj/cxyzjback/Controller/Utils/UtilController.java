package com.cxyzj.cxyzjback.Controller.Utils;

import com.cxyzj.cxyzjback.Service.Interface.Other.UtilService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther: 夏
 * @DATE: 2018/9/12 13:50
 * @Description:
 */

@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "/v1/utils")
public class UtilController {

    @Autowired
    private UtilService utilService;

    @PostMapping(value = "/upload/{type}")
    @PreAuthorize("hasAnyRole('ROLE_ANONYMITY','ROLE_USER','ROLE_ADMIN','ROLE_ADMINISTRATORS')")
    public String fileUpload(@RequestParam MultipartFile file, @PathVariable(name = "type") String type) {
        return utilService.fileUpload(file,type);
    }

}
