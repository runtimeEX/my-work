package com.shiro.conf.cache;

import com.shiro.utils.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.conf.cache
 * @Description: TODO
 * @date Date : 2021年06月18日 上午11:55
 */
@Slf4j
public class RedisCacheManager implements CacheManager {
    private CacheService cacheService;
    /**
     * 缓存过期时间，单位秒
     */
    private int cacheExp = -1;
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();
    public void setCacheExp(int cacheExp) {
        this.cacheExp = cacheExp;
    }

    public RedisCacheManager setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
        return this;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        log.info("get cache, name={}",name);
        Cache cache = caches.get(name);
        if (null==cache){
            cache = new RedisCache<K,V>(cacheService, cacheExp);
            caches.put(name,cache);
        }
        return cache;
    }
}
