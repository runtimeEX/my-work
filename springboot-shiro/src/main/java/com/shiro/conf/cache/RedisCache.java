package com.shiro.conf.cache;

import com.shiro.utils.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.conf.cache
 * @Description: TODO
 * @date Date : 2021年06月18日 上午11:54
 */
@Slf4j
public class RedisCache<K, V> implements Cache<K, V> {

    private final CacheService cacheService;


    public RedisCache(CacheService cacheService, int cacheExp) {
        this.cacheService = cacheService;
        this.cacheExp = cacheExp;
    }

    /**
     * 标识shiro缓存前缀
     */
    public static final String CACHE_PREFIX = "shiro:";
    /**
     * 缓存过期时间，单位秒
     */
    private int cacheExp = -1;

    @Override
    public V get(K k) throws CacheException {
        return (V) cacheService.get(CACHE_PREFIX + k);
    }

    @Override
    public V put(K k, V v) throws CacheException {
        if (cacheService.set(CACHE_PREFIX + k, v, cacheExp)) {
            return v;
        }
        return null;
    }

    @Override
    public V remove(K k) throws CacheException {
        V v = get(k);
        cacheService.del(CACHE_PREFIX+k);
        log.info("remove cache key:{},value:{}",CACHE_PREFIX+k,v);
        return v;
    }

    @Override
    public void clear() throws CacheException {
        cacheService.del(String.valueOf(keys()));
    }

    @Override
    public int size() {
        return cacheService.keys(CACHE_PREFIX + "*").size();
    }

    @Override
    public Set<K> keys() {
        return cacheService.keys(CACHE_PREFIX + "*");
    }

    @Override
    public Collection<V> values() {
        List<V> list = new ArrayList<>();
        Set<String> keys = (Set<String>) keys();
        keys.forEach(k -> {
            V value = get((K) k);
            if (value != null) {
                list.add(value);
            }
        });
        return list;
    }
}
