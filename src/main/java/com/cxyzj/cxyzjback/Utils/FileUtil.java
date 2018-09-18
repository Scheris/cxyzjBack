package com.cxyzj.cxyzjback.Utils;

import org.apache.coyote.http11.Constants;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

/**
 * @Auther: 夏
 * @DATE: 2018/9/12 13:48
 * @Description: 文件上传
 */
public class FileUtil {

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }
}
