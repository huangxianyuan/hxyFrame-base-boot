package com.hxy.config;

import com.hxy.component.redis.CachingShiroSessionDao;
import com.hxy.component.redis.ShiroSessionListener;
import com.hxy.component.shiro.MyRealm;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.*;

/**
 * 类ShiroConfig的功能描述:
 * Shiro配置
 * @auther hxy
 * @date 2017-11-15 21:50:12
 */
@Configuration
public class ShiroConfig {

    @Bean("sessionIdCookie")
    public Cookie simpleCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        //设置了HttpOnly属性，那么通过js脚本将无法读取到cookie信息，这样能有效的防止XSS攻击
        simpleCookie.setHttpOnly(true);
        //maxAge=-1 表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    @Bean("sessionManager")
    public SessionManager sessionManager(Cookie sessionIdCookie, CachingShiroSessionDao sessionDAO, ShiroSessionListener shiroSessionListener){
        sessionDAO.setPrefix("shiro-session:");
        //注意中央缓存有效时间要比本地缓存有效时间长
        sessionDAO.setSeconds(1800);
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //设置全局会话超时时间，默认30分钟(1800000)
        sessionManager.setGlobalSessionTimeout(60 * 60 * 1000);
        //是否在会话过期后会调用SessionDAO的delete方法删除会话 默认true
        sessionManager.setDeleteInvalidSessions(false);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        //是否开启会话验证器任务 默认true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //会话验证器调度时间
        sessionManager.setSessionValidationInterval(1800000);
        sessionManager.setSessionIdCookieEnabled(true);

        sessionManager.setSessionIdCookie(sessionIdCookie);
        sessionManager.setSessionDAO(sessionDAO);
        List<SessionListener> sessionListeners = new ArrayList<>();
        sessionListeners.add(shiroSessionListener);
        sessionManager.setSessionListeners(sessionListeners);
        return sessionManager;
    }

  /*  @Bean("cacheManager")
    public CacheManager cacheManager(){
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        Resource res=new ClassPathResource("ehcache.xml");
        cacheManagerFactoryBean.setConfigLocation(res);
        return cacheManagerFactoryBean.getObject();
    }


    @Bean("shiroCacheManager")
    public EhCacheManager ehCacheManager(CacheManager cacheManager){
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManager(cacheManager);
        return ehCacheManager;
    }*/


    @Bean("securityManager")
    public SecurityManager securityManager(MyRealm myRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        //Shiro的核心安全接口,这个属性是必须的
        shiroFilter.setSecurityManager(securityManager);
        shiroFilter.setLoginUrl("/login.html");
        shiroFilter.setSuccessUrl("/index.html");
        //shiro不拦截资源
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/druid/**", "anon");
        filterMap.put("/app/**", "anon");
        filterMap.put("/login/login", "anon");
        filterMap.put("/**/*.css", "anon");
        filterMap.put("/**/*.js", "anon");
        filterMap.put("/*.html", "anon");
        filterMap.put("/fonts/**", "anon");
        filterMap.put("/images/**", "anon");
        filterMap.put("/plugins/**", "anon");
        filterMap.put("/login/captcha", "anon");
        filterMap.put("/statics/**", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/", "anon");
        filterMap.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    /**
     * 保证实现了Shiro内部lifecycle函数的bean执行
     * @return
     */
    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * AOP式方法级权限检查
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
