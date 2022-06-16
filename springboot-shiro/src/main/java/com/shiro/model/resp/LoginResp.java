package com.shiro.model.resp;

import lombok.Data;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.model.resp
 * @Description: TODO
 * @date Date : 2021年06月19日 下午4:56
 */
@Data
public class LoginResp {
    private String token;
    private String userStatus;
}
