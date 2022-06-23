package com.boss.wx.qrcode;

import lombok.Data;

@Data
public class UploadRequest {
    /**
     * 是否覆盖 0.否 1.是
     */
    private Integer isCover = 1;

    /**
     * 要存储的文件名称
     */
    private String fileName;

    /**
     * 时间戳
     */
    private long timestamp;

}
