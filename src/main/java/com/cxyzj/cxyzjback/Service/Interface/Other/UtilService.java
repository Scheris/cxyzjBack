package com.cxyzj.cxyzjback.Service.Interface.Other;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther: 夏
 * @DATE: 2018/9/12 14:00
 * @Description:
 */
public interface UtilService {
    String fileUpload(MultipartFile multipartFile,String type);
}
