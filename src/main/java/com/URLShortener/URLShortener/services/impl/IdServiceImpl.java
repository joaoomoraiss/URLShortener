package com.URLShortener.URLShortener.services.impl;

import com.URLShortener.URLShortener.exceptions.InvalidIncrementId;
import com.URLShortener.URLShortener.services.IdService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class IdServiceImpl implements IdService {

    private static final String KEY = "url:global:id";
    private static final Long INITIAL_VALUE = 985156648L;

    private final RedisTemplate<String, String> redisTemplate;

    public IdServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Long nextId() {
        redisTemplate.opsForValue().setIfAbsent(KEY, String.valueOf(INITIAL_VALUE));

        Long id = redisTemplate.opsForValue().increment(KEY);

        if (id == null) {
            throw new InvalidIncrementId();
        }

        return id;
    }
}
