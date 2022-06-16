package com.rabbit.seckill.conf;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class RedisCacheConfig {
    private final static Logger log = LoggerFactory.getLogger(RedisCacheConfig.class);

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);

        // 值采用json序列化
        template.setValueSerializer(jacksonSeial);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());

        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSeial);
        //必须执行这个函数,初始化RedisTemplate
        template.afterPropertiesSet();
        log.info("RedisTemplate注入成功！！");
        return template;
    }

    /**
     * Jedis  springboot 2.0X已经移除
     * 单机版
     *
     * @return
     */
   /* @Bean
    public JedisPool redisPoolFactory() {

        JedisPool pool = new JedisPool(poolCofig(), "aoldman.com", 6379, 2000, "zhouhao");
        log.info("JedisPool注入成功！！");
        return pool;
    }

    public JedisPoolConfig poolCofig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(200);
        config.setMaxIdle(50);
        config.setMinIdle(8);//设置最小空闲数
        config.setMaxWaitMillis(10000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        //Idle时进行连接扫描
        config.setTestWhileIdle(true);
        //表示idle object evitor两次扫描之间要sleep的毫秒数
        config.setTimeBetweenEvictionRunsMillis(30000);
        //表示idle object evitor每次扫描的最多的对象数
        config.setNumTestsPerEvictionRun(10);
       //表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
        config.setMinEvictableIdleTimeMillis(60000);

        return config;
    }

    *//**
     * 集群版
     *
     * @return
     *//*
    @Bean
    public JedisCluster redisClusterFactory() {
        Set<HostAndPort> node = new HashSet<>();
        node.add(new HostAndPort("114.55.252.184", 7001));
        node.add(new HostAndPort("114.55.252.184", 7002));
        node.add(new HostAndPort("114.55.252.184", 7003));
        node.add(new HostAndPort("114.55.252.184", 7004));
        node.add(new HostAndPort("114.55.252.184", 7005));
        node.add(new HostAndPort("114.55.252.184", 7006));
        //maxAttempts 出现异常最大重试次数 soTimeOut 读取数据时间
        JedisCluster jedisCluster = new JedisCluster(node, 2000, 2000, 10, "zhouhao", poolCofig());
        log.info("JedisCluster集群");
        return jedisCluster;
    }
*/
    @Bean
    public Redisson redisson() {
        //此为单机模式
        Config config = new Config();
        config.useSingleServer().setAddress("redis://runtimex.vip:6379").setPassword("zhouhao").setDatabase(8);
        return (Redisson) Redisson.create(config);
    }


}
