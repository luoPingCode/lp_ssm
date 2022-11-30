package com.lp.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lp.configuration.CustomObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class RedisTemplate {
    @Resource
    private JedisPool jedisPool;
    @Resource
    private CustomObjectMapper objectMapper;//序列或反序列化方法
//保存字符串数据类型  expire 过期时间
    public String set(String key,String value,long expire){
        Jedis jedis = jedisPool.getResource();
        String returnValue = "";
        try{
            returnValue = jedis.setex(key, expire, value);
        }catch (JedisException jedisException){
            log.error("redis execution error",jedisException);
            jedisPool.returnBrokenResource(jedis);
        }finally {
            jedisPool.returnResource(jedis); //向jedis返回资源
        }
        return returnValue;
    }
//    获取Redis
    public String get(String key){
        Jedis jedis = jedisPool.getResource();
        String returnValue = null;
        try{
            returnValue = jedis.get(key);
        }catch (JedisException jedisException){
            log.error("redis execution error",jedisException);
            jedisPool.returnBrokenResource(jedis);
        }finally {
            jedisPool.returnResource(jedis); //向jedis返回资源
        }
        return returnValue;
    }
//    将对象以序列化的方式存在Redis,json
    public String setObject(String key,Object value,long expire){
        Jedis jedis = jedisPool.getResource();
        String returnValue = "";
        try{
//            value序列化成JSON
            String jsonValue = objectMapper.writeValueAsString(value);
            if (expire <= 0){
                returnValue = jedis.set(key, jsonValue);//永不过期
            }else {
                returnValue = jedis.setex(key, expire, jsonValue);
            }

        }catch (JedisException | JsonProcessingException jedisException){
            log.error("redis execution error",jedisException);
            jedisPool.returnBrokenResource(jedis);
        }finally {
            jedisPool.returnResource(jedis); //向jedis返回资源
        }
        return returnValue;
    }

    public <T> T getObject(String key, TypeReference<T> typeReference){
        Jedis jedis = jedisPool.getResource();
        T object = null;
        try{
//从Redis中获取
            log.info("key="+key);
//            log.info("typeReference"+typeReference);
            String returnValue = jedis.get(key);
//            log.info("returnValue"+returnValue);
            object = objectMapper.readValue(returnValue,typeReference);
        }catch (JedisException | JsonProcessingException  jedisException){
            log.error("redis execution error",jedisException);
            jedisPool.returnBrokenResource(jedis);
        }finally {
            jedisPool.returnResource(jedis); //向jedis返回资源
        }
        return object;
    }
    //删除Redis中数据
    public long remove(String ...key){
        Jedis jedis = jedisPool.getResource();
        long returnValue = 0L; //表示一个都没有删掉
        try{
            returnValue = jedis.del(key);
        }catch (JedisException jedisException){
            log.error("redis execution error",jedisException);
            jedisPool.returnBrokenResource(jedis);
        }finally {
            jedisPool.returnResource(jedis); //向jedis返回资源
        }
        return returnValue;
    }
//    每隔一段时间增加，token失效时间
    public long expire(String key, Long tokenTime){
        Jedis jedis = jedisPool.getResource();
        long returnValue = -1L;
        try{
//从Redis中获取
//            log.info("key=" , key);
//            log.info("typeReference",typeReference);
             returnValue = jedis.expire(key,tokenTime);
             log.info(String.valueOf(returnValue));
//            log.info("returnValue",returnValue);
//            object = objectMapper.readValue(returnValue,typeReference);
        }catch (JedisException jedisException){
            log.error("redis execution error",jedisException);
            jedisPool.returnBrokenResource(jedis);
        }finally {
            jedisPool.returnResource(jedis); //向jedis返回资源
        }
        return returnValue;
    }
//    查询key
    public Set<String> keys(String keys){
        Jedis jedis = jedisPool.getResource();
        Set<String> returnValue = null;
        try{
            returnValue = jedis.keys(keys);
//            log.info(String.valueOf(returnValue));
        }catch (JedisException jedisException){
            log.error("redis execution error",jedisException);
            jedisPool.returnBrokenResource(jedis);
        }finally {
            jedisPool.returnResource(jedis); //向jedis返回资源
        }
        return returnValue;
    }
}
