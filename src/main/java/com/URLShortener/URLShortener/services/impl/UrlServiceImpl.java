package com.URLShortener.URLShortener.services.impl;

import com.URLShortener.URLShortener.domain.CreateUrlRequest;
import com.URLShortener.URLShortener.domain.entities.Url;
import com.URLShortener.URLShortener.exceptions.InvalidShortCode;
import com.URLShortener.URLShortener.exceptions.OriginalUrlIsNotUrl;
import com.URLShortener.URLShortener.repositories.jpa.UrlRepository;
import com.URLShortener.URLShortener.services.IdService;
import com.URLShortener.URLShortener.services.UrlService;
import org.hashids.Hashids;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final IdService idService;

    public UrlServiceImpl(UrlRepository urlRepository, RedisTemplate<String, String> redisTemplate, IdService idService) {
        this.urlRepository = urlRepository;
        this.redisTemplate = redisTemplate;
        this.idService = idService;
    }

    @Override
    public Url createShortUrl(CreateUrlRequest request) {

        if (request.getOriginalUrl() == null || request.getOriginalUrl().isEmpty()) {
            throw new IllegalArgumentException("Original URL cannot be null or empty");
        }

        // check if is url
        String urlPattern = "^(https?:\\/\\/)?(?:www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_\\+.~#?&//=]*)$";
        if (!request.getOriginalUrl().matches(urlPattern)) {
            throw new OriginalUrlIsNotUrl();
        }

        //generate unique id
        long uniqueId = idService.nextId();

        //encrypt
        Hashids hashids = new Hashids("secret", 7); //change in prod
        String shortCode = hashids.encode(uniqueId);

        LocalDateTime now = LocalDateTime.now();
        Url newUrl = new Url(
                shortCode,
                request.getOriginalUrl(),
                0L,
                now,
                now
        );

        urlRepository.save(newUrl);

        return newUrl;
    }

    @Override
    public String redirectToOriginalUrl(String shortCode) {

        if (shortCode == null || shortCode.isEmpty()) {
            throw new IllegalArgumentException("Short code cannot be null or empty");
        }

        Url url = urlRepository.findById(shortCode)
                .orElseThrow(InvalidShortCode::new);

        String cacheKey = "short:url:" + shortCode;

        // get in cache
        String cachedUrl = redisTemplate.opsForValue().get(cacheKey);
        if (cachedUrl != null) {
            url.setClickCount(url.getClickCount() + 1);
            urlRepository.save(url);
            return cachedUrl;
        }


        // +1 in clicks
        try {
            url.setClickCount(url.getClickCount() + 1);
            urlRepository.save(url);
        } catch (Exception e) {
            throw new InvalidShortCode("Invalid short code: " + e);
        }

        // if clicks > 1000, cache in redis
        if (url.getClickCount() != null && url.getClickCount() > 1000) {
            redisTemplate.opsForValue().set(cacheKey, url.getOriginalUrl());
        }

        return url.getOriginalUrl();
    }
}
