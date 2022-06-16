package com.shiro.conf;

import com.shiro.entity.SysPermission;
import com.shiro.entity.SysRolePermission;
import com.shiro.entity.SysUser;
import com.shiro.entity.SysUserRole;
import com.shiro.service.SysPermissionService;
import com.shiro.service.SysRolePermissionService;
import com.shiro.service.SysUserRoleService;
import com.shiro.service.SysUserService;
import com.shiro.utils.CacheService;
import com.shiro.utils.JwtUtils;
import com.shiro.utils.RequestContext;
import com.shiro.utils.WebContextFacade;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.conf
 * @Description: TODO
 * @date Date : 2021年06月18日 上午10:24
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private SysRolePermissionService sysRolePermissionService;
    @Autowired
    private CacheService cacheService;

    public static final String SHIRO_CACHE_PREFIX = "shiro:token:";

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null;
    }

    /**
     * 授权
     * 先查询缓存中是否有相应的权限信息，如果没有，则走此方法
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String jwtToken = (String) getAvailablePrincipal(principals);
        Claims claims = JwtUtils.checkJWT(jwtToken);
        if (claims == null) {
            throw new AuthenticationException("token无效");
        }
        SysUser user = sysUserService.queryById(Long.valueOf(claims.getSubject()));
        if (null == user) {
            return null;
        }
        List<String> permissionCodeList;
        SysUserRole sysUserRoleQuery = new SysUserRole();
        sysUserRoleQuery.setUserId(user.getId());
        //根据用户id，获取当前用户的角色
        List<SysUserRole> sysUserRoleList = sysUserRoleService.list(sysUserRoleQuery);
        boolean isAdmin = sysUserRoleList.stream().anyMatch(userRole -> userRole.getRoleId() == 0);
        info.setRoles(sysUserRoleList.stream().map(userRole -> userRole.getRoleId().toString()).collect(Collectors.toSet()));
        if (isAdmin) {
            permissionCodeList = sysPermissionService.selectAll().stream().map(SysPermission::getCode).collect(Collectors.toList());
        } else {
            //如果不是管理员用户，查询该用户所有角色id
            List<Long> roleIds = sysUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            //根据角色id列表查询所有权限id
            List<Long> permissionIds = sysRolePermissionService.selectByRoleIdList(roleIds).stream().map(SysRolePermission::getPermissionId).collect(Collectors.toList());
            //根据权限id列表查询所有权限code
            permissionCodeList = sysPermissionService.selectByIdList(permissionIds).stream().map(SysPermission::getCode).collect(Collectors.toList());
        }
        info.addStringPermissions(permissionCodeList);
        return info;
    }

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        //使用jwt时，不需要用户的用户名密码了，只要校验token即可
        Claims claims = JwtUtils.checkJWT(token);
        if (claims == null) {
            log.info("token无效");
            throw new AuthenticationException("token无效");
        }

        String userId = claims.getSubject();
        //判断用户是否退出登录
        if (null == cacheService.get(SHIRO_CACHE_PREFIX + claims.getSubject())) {
            log.info("请重新登录");
            throw new AuthenticationException("请重新登录");
        }
        RequestContext requestContext = WebContextFacade.getRequestContext();
        requestContext.setUserId(Long.parseLong(userId));
        WebContextFacade.setRequestContext(requestContext);
        return new SimpleAuthenticationInfo(authenticationToken.getPrincipal(), authenticationToken.getCredentials(), getName());
    }
}
