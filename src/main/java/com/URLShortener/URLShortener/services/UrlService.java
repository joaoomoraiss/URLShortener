package com.URLShortener.URLShortener.services;

import com.URLShortener.URLShortener.domain.CreateUrlRequest;
import com.URLShortener.URLShortener.domain.RedirectUrlRequest;
import com.URLShortener.URLShortener.domain.entities.Url;

public interface UrlService {
    Url createShortUrl(CreateUrlRequest request);
    String redirectToOriginalUrl(String shortCode);
}
