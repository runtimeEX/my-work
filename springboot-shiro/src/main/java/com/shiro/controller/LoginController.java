package com.shiro.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiro.entity.SysUser;
import com.shiro.model.resp.LoginResp;
import com.shiro.service.SysUserService;
import com.shiro.utils.*;
import com.shiro.utils.parm.impl.BaseCommonParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.shiro.conf.ShiroRealm.SHIRO_CACHE_PREFIX;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.controller
 * @Description: TODO
 * @date Date : 2021年06月18日 下午3:21
 */
@RestController
@RequestMapping("/app")
@Slf4j
public class LoginController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CacheService cacheService;

    @GetMapping("/login")
    public String login() {
        return "登录页面";
    }

    @GetMapping("/test")
    @RequiresPermissions("test")
    public void test() {
        Long userId = WebContextFacade.getRequestContext().getUserId();
        System.out.println("-----test:" + userId);

        BaseCommonParam commonParam = WebContextFacade.getRequestContext().getCommonParam();
        System.out.println(JSONUtil.toJsonStr(commonParam));
    }

    @GetMapping("/doLogin")
    public ResultInfo doLogin(SysUser user) {
        SysUser existUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getAccount, user.getAccount()));
        if (existUser == null) {
            log.info("账号不存在");
            return new ResultInfo().message("账号不存在");
        }
        if (!user.getPhone().equals(existUser.getPhone())) {
            log.info("手机号不正确");
            return new ResultInfo().message("手机号不正确");
        }
        //自己验证密码，不交给shiro校验
        ByteSource salt = ByteSource.Util.bytes(existUser.getSalt());
        SimpleHash simpleHash = new SimpleHash("md5", user.getPassword(), salt, 6);
        if (!existUser.getPassword().equals(simpleHash.toString())) {
            log.info("密码不正确");
            return new ResultInfo().message("密码不正确");
        }
        //生成token
        String token = JwtUtils.generateJsonWebToken(String.valueOf(existUser.getId()));
        //此缓存用于限制用户退出登录后，需要重新登录才能访问接口，而不是带个有效token就可以访问
        cacheService.set(SHIRO_CACHE_PREFIX + existUser.getId(), token, 30 * 60 * 1000);
        SecurityUtils.getSubject().login(new AuthenticationToken() {
            @Override
            public Object getPrincipal() {
                return token;
            }

            @Override
            public Object getCredentials() {
                return token;
            }
        });
        LoginResp loginResp = new LoginResp();
        loginResp.setToken(token);
        return new ResultInfo(Status.SUCCESS, loginResp);
    }

    /**
     * 退出登录
     *
     * @return httpResponse
     */
    @PostMapping("/logout")
    public ResultInfo logout() {
        PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
        if (principalCollection != null) {
            Long userId = WebContextFacade.getRequestContext().getUserId();
            cacheService.del(SHIRO_CACHE_PREFIX + userId);
        }
        //清除权限缓存
        SecurityUtils.getSubject().logout();
        return new ResultInfo(Status.SUCCESS);
    }


}
