package com.cxyzj.cxyzjback.Service.impl.Other;

import com.cxyzj.cxyzjback.Service.Interface.Other.UtilService;
import com.cxyzj.cxyzjback.Utils.Response;
import com.cxyzj.cxyzjback.Utils.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @Auther: 夏
 * @DATE: 2018/9/12 14:01
 * @Description: 工具
 */
@Service
@Slf4j

public class UtilServiceImpl implements UtilService {

    private Response response;
    //读取配置文件中的路径信息
    @Value("${filePath.avatar}")
    private String avatarPath;
    @Value("${filePath.background}")
    private String backgroundPath;
    @Value("${filePath.article}")
    private String articlePath;
    @Value("${filePath.discussion}")
    private String discussionPath;
    @Value("${filePath.other}")
    private String otherPath;
    @Value("${filePath.temp}")
    private String tempPath;

    /**
     * @param multipartFile 文件
     * @param type          文件的分类
     * @return 文件上传的地址
     * @checked true
     */
    @Override
    public String fileUpload(MultipartFile multipartFile, String type) {

        response = new Response();

        if (multipartFile.isEmpty()) {
            return response.sendFailure(Status.FILE_UPLOAD_FAILURE, "文件不能为空");
        }
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = null;
        if (fileName != null) {
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }
        // 文件上传后的路径，临时路径，部署到服务器需要修改
        String filePath;
        switch (type) {//根据类型选择不同的路径文件夹
            case "avatar":
                filePath = articlePath;
                break;
            case "background":
                filePath = backgroundPath;
                break;
            case "article":
                filePath = articlePath;
                break;
            case "discussion":
                filePath = discussionPath;
                break;
            case "other":
                filePath = otherPath;
                break;
            default:
                filePath = tempPath;
                break;
        }
        fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        log.info("上传的后缀名为：" + suffixName);
        log.info("上传的文件名为：" + fileName);
        log.info("目标存储位置：" + dest.getPath());
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();//目录不存在，进行创建
        }
        try {
            if (dest.createNewFile() && dest.canWrite()) {
                FileOutputStream outputStream = new FileOutputStream(dest);
                outputStream.write(multipartFile.getBytes());
                outputStream.close();//关闭文件流
                response.insert("url", dest.getPath());
                return response.sendSuccess();
            }
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return response.sendFailure(Status.FILE_UPLOAD_FAILURE, "文件上传失败");
        }
        return response.sendFailure(Status.FILE_UPLOAD_FAILURE, "文件上传失败");
    }
}
