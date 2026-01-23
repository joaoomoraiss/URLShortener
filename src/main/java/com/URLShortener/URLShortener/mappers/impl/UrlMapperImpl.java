package com.URLShortener.URLShortener.mappers.impl;

import com.URLShortener.URLShortener.domain.dto.UrlDto;
import com.URLShortener.URLShortener.domain.entities.Url;
import com.URLShortener.URLShortener.mappers.UrlMapper;
import org.springframework.stereotype.Component;

@Component
public class UrlMapperImpl implements UrlMapper {

    @Override
    public UrlDto toDto(Url url) {
        return new UrlDto(
                url.getShortCode(),
                url.getOriginalUrl()
        );
    }
}
