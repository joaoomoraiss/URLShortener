package com.URLShortener.URLShortener.mappers;

import com.URLShortener.URLShortener.domain.dto.UrlDto;
import com.URLShortener.URLShortener.domain.entities.Url;

public interface UrlMapper {

    UrlDto toDto(Url url);
}
