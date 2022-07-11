package com.jianduanqingchang.securityservice.component;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Redis tools
 * @author yujie
 */
@Component
public class RedisComponent {
    
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 从字符串获取值
     *
     * @param key key
     */
    public Object get(String key) {
        return this.stringRedisTemplate.opsForValue().get(key);
    }
    
    /**
     * 字符串存入值
     * 默认过期时间为2小时
     *
     * @param key key
     */
    public void set(String key, String value) {
        this.stringRedisTemplate.opsForValue().set(key, value, 7200, TimeUnit.SECONDS);
    }
    
    /**
     * 字符串存入值
     *
     * @param expire 过期时间（毫秒计）
     * @param key    key
     * @param value  value
     */
    public void set(String key, String value, Integer expire) {
        this.stringRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }
    
    /**
     * 删除key
     * 这里跟下边deleteKey()最底层实现都是一样的，应该可以通用
     *
     * @param key key
     */
    public void delete(String key) {
        this.stringRedisTemplate.opsForValue().getOperations().delete(key);
    }
    
    /**
     * 添加单个
     * 默认过期时间为两小时
     *
     * @param key    key
     * @param filed  filed
     * @param domain 对象
     */
    public void hSet(String key, String filed, Object domain) {
        this.stringRedisTemplate.opsForHash().put(key, filed, domain);
    }
    
    /**
     * 添加单个
     *
     * @param key    key
     * @param filed  filed
     * @param domain 对象
     * @param expire 过期时间（毫秒计）
     */
    public void hSet(String key, String filed, Object domain, long expire) {
        this.stringRedisTemplate.opsForHash().put(key, filed, domain);
        this.stringRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }
    
    /**
     * 添加HashMap
     *
     * @param key key
     * @param hm  要存入的hash表
     */
    public void hSet(String key, Map<String, Object> hm) {
        this.stringRedisTemplate.opsForHash().putAll(key, hm);
    }
    
    /**
     * 如果key存在就不覆盖
     *
     * @param key    key
     * @param filed  field
     * @param domain domain
     */
    public void hetAbsent(String key, String filed, Object domain) {
        this.stringRedisTemplate.opsForHash().putIfAbsent(key, filed, domain);
    }
    
    /**
     * 查询key和field所确定的值
     *
     * @param key   查询的key
     * @param field 查询的field
     * @return HV
     */
    public Object hGet(String key, String field) {
        return this.stringRedisTemplate.opsForHash().get(key, field);
    }
    
    /**
     * 查询该key下所有值
     *
     * @param key 查询的key
     * @return Map<HK, HV>
     */
    public Object hGet(String key) {
        return this.stringRedisTemplate.opsForHash().entries(key);
    }
    
    /**
     * 删除key下所有值
     *
     * @param key 查询的key
     */
    public void deleteKey(String key) {
        this.stringRedisTemplate.opsForHash().getOperations().delete(key);
    }
    
    /**
     * 判断key和field下是否有值
     *
     * @param key   判断的key
     * @param field 判断的field
     */
    public Boolean hasKey(String key, String field) {
        return this.stringRedisTemplate.opsForHash().hasKey(key, field);
    }
    
    /**
     * 判断key下是否有值
     *
     * @param key 判断的key
     */
    public boolean hasKey(String key) {
        Boolean hasKey = this.stringRedisTemplate.opsForHash().getOperations().hasKey(key);
        return Objects.requireNonNullElse(hasKey, false);
    }
}