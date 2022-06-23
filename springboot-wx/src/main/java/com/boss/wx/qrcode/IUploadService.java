package com.boss.wx.qrcode;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-wx
 * @Package com.boss.wx.qrcode
 * @Description: TODO
 * @date Date : 2021年09月24日 下午6:04
 */
@Service
public class IUploadService {
    protected static final DateTimeFormatter FILE_NAME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");

    public UploadSuccess upload(InputStream uploadStream, UploadRequest uploadRequest) throws IOException {
        //验证签名
      //  signValid(uploadRequest, token);
        String fileName = getFileName(uploadRequest, null);
        File file = new File("/Users/zhouhao/Downloads", fileName);
        //验证父目录是否存在
        if (!parentDirValid(file)) {
            return new UploadSuccess();
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(StreamUtils.copyToByteArray(uploadStream));
        }
        return uploadSuccess(fileName);
    }
    private UploadSuccess uploadSuccess(String fileName) {
        UploadSuccess uploadSuccess = new UploadSuccess();
        uploadSuccess.setUrl("/media/" + fileName);
        return uploadSuccess;
    }
    /**
     * 验证父目录是否存在
     */
    private boolean parentDirValid(File file) {
        if (file.getParentFile().exists()) {
            return true;
        } else {
            return file.getParentFile().mkdirs();
        }
    }
    /**
     * 根据上传参数获取文件名称
     *
     * @param uploadRequest 上传参数
     * @return 文件名
     */
    protected String getFileName(UploadRequest uploadRequest, MultipartFile multipartFile) {
        String suffix = multipartFile != null ? Utils.fileSuffixWithPoint(multipartFile.getOriginalFilename()) : "";
        String fileName = StringUtils.isEmpty(uploadRequest.getFileName()) ? Utils.uuid() + suffix : uploadRequest.getFileName();
        //如果不覆盖,在文件后增加时间后缀
        if (uploadRequest.getIsCover() == 0) {
            fileName = fileName.concat(LocalDateTime.now().format(FILE_NAME_FORMATTER));
        }
        return fileName;
    }
}
