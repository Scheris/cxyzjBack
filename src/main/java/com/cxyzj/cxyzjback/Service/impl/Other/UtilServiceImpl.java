package com.cxyzj.cxyzjback.Service.impl.Other;

import com.cxyzj.cxyzjback.Service.Interface.Other.UtilService;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @Auther: 夏
 * @DATE: 2018/9/12 14:01
 * @Description:
 */
@Service
@Slf4j
public class UtilServiceImpl implements UtilService {

    private Response response;

    @Override
    public String fileUpload(MultipartFile multipartFile) {

        response = new Response();

        if (multipartFile.isEmpty()) {
            return response.sendFailure(Status.FILE_UPLOAD_FAILURE, "文件不能为空");
        }
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 文件上传后的路径，临时路径，部署到服务器需要修改
        String filePath = "pics/";
        // 解决中文问题，liunx下中文路径，图片显示问题
        fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        log.info("上传的后缀名为：" + suffixName);
        log.info("上传的文件名为：" + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            multipartFile.transferTo(dest);
            return response.sendSuccess();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.sendFailure(Status.FILE_UPLOAD_FAILURE, "文件上传失败");
    }
}
