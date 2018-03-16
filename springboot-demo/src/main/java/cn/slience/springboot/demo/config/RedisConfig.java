/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.springboot.demo.config;

import org.springframework.context.annotation.Configuration;

/**
 *
 * @author breeze
 */
//@Configuration
public class RedisConfig {
    
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
//        jedisConnectionFactory.setHostName("127.0.0.1");
//        jedisConnectionFactory.setPort(6379);
//        return jedisConnectionFactory;
//    }
//    
//     @Bean
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        Jackson2JsonRedisSerializer<Object> serializer = jackson2JsonRedisSerializer();
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(serializer);
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(serializer);
//        return redisTemplate;
//    }
//    
//    
//     @Bean
//    public CacheManager redisCacheManager() {
//        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
//        cacheManager.setDefaultExpiration(300);
//        cacheManager.setLoadRemoteCachesOnStartup(true); // 启动时加载远程缓存
//        cacheManager.setUsePrefix(true); //是否使用前缀生成器
//        // 这里可进行一些配置 包括默认过期时间 每个cacheName的过期时间等 前缀生成等等
//        return cacheManager;
//    }
    
//     @Bean
//    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
//        final Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        final ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
//            .json().build();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//        return jackson2JsonRedisSerializer;
//    }
    
}
