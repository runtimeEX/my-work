package com.shiro.conf;

import com.shiro.conf.cache.RedisCacheManager;
import com.shiro.filter.JWTFilter;
import com.shiro.utils.CacheService;
import com.shiro.utils.ResultInfo;
import com.shiro.utils.Status;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.conf
 * @Description: TODO
 * @date Date : 2021年06月18日 上午10:20
 */
@Configuration
@ConditionalOnProperty(prefix = "app.shiro", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ShiroProperties.class)
@Slf4j
public class ShiroConfig {
    //生命周期
    @Bean
    @ConditionalOnMissingBean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /*
     * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * 所以我们需要修改下doGetAuthenticationInfo中的代码; )
     * 使用jwt时，不需要密码验证了
     */
 /*   @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(6);// 散列的次数，比如散列两次，相当于md5(md5(""));
        return hashedCredentialsMatcher;
    }*/

    @Bean
    @ConditionalOnMissingBean
    public AuthorizingRealm authorizingRealm() {
        ShiroRealm realm = new ShiroRealm();
        //     realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }

    /**
     * @return 缓存管理器
     */
    @Bean
    @ConditionalOnMissingBean
    public CacheManager shiroCacheManager(ShiroProperties shiroProperties, CacheService cacheService) {
        RedisCacheManager shiroCacheManager = new RedisCacheManager();
        shiroCacheManager.setCacheExp(shiroProperties.getCacheExp());
        shiroCacheManager.setCacheService(cacheService);
        return shiroCacheManager;
    }

    /**
     * @param authorizingRealm shiroRealm
     * @return 权限管理器
     */
    @Bean
    @ConditionalOnMissingBean
    public SecurityManager securityManager(AuthorizingRealm authorizingRealm, ShiroProperties shiroProperties, CacheService cacheService) {
        authorizingRealm.setCacheManager(shiroCacheManager(shiroProperties, cacheService));
        //启用授权缓存，即缓存AuthorizationInfo信息，默认false
        authorizingRealm.setAuthorizationCachingEnabled(true);
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(authorizingRealm);
        securityManager.setCacheManager(shiroCacheManager(shiroProperties, cacheService));
        //关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions)
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * @param securityManager 权限管理器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 过滤器
     *
     * @param securityManager 权限管理器
     * @param shiroProperties shiro配置
     * @return 过滤器
     */
    @Bean
    @ConditionalOnMissingBean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, ShiroProperties shiroProperties) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroProperties.getFilterChainDefinitionMap());
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("jwt", new JWTFilter());
        shiroFilterFactoryBean.setFilters(filters);
        // 设置无权限时跳转的 url;
        //只有perms，roles，ssl，rest，port才是属于AuthorizationFilter，而anon，authcBasic，auchc，user是AuthenticationFilter，所以unauthorizedUrl设置后页面不跳转。
        //  shiroFilterFactoryBean.setUnauthorizedUrl("/app/unauthorized");
        //身份认证没通过时跳转的url
        shiroFilterFactoryBean.setLoginUrl("/app/login");
        return shiroFilterFactoryBean;
    }

    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(Exception.class)
        @ResponseBody
        public ResultInfo handleException(Exception exception) {
            log.info("异常捕获:{}",exception.getMessage());
            if (exception instanceof UnauthorizedException) {
                return new ResultInfo(Status.INSUFFICIENT_PERMISSION);
            }
            if (exception instanceof UnauthenticatedException) {
                return new ResultInfo(Status.LOGIN_EXPIRE);
            }
            return new ResultInfo(Status.SYSTEM_ERROR);
        }
    }

}
