package com.URLShortener.URLShortener.services.impl;

import com.URLShortener.URLShortener.domain.CreateUrlRequest;
import com.URLShortener.URLShortener.domain.RedirectUrlRequest;
import com.URLShortener.URLShortener.domain.entities.Url;
import com.URLShortener.URLShortener.exceptions.InvalidShortCode;
import com.URLShortener.URLShortener.exceptions.OriginalUrlIsNotUrl;
import com.URLShortener.URLShortener.repositories.UrlRepository;
import com.URLShortener.URLShortener.services.UrlService;
import org.hashids.Hashids;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
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

        long initialCount = urlRepository.count() + 985156648L;

        //encrypt
        Hashids hashids = new Hashids("secret", 5);
        String shortCode = hashids.encode(initialCount);

        LocalDateTime now = LocalDateTime.now();
        Url newUrl = new Url(
                shortCode,
                request.getOriginalUrl(),
                0L,
                now,
                now
        );

        return urlRepository.save(newUrl);
    }

    @Override
    public String redirectToOriginalUrl(String shortCode) {

        if (shortCode == null || shortCode.isEmpty()) {
            throw new IllegalArgumentException("Original URL cannot be null or empty");
        }

        Url url = urlRepository.findById(shortCode)
                .orElseThrow(() -> new InvalidShortCode());

        return url.getOriginalUrl();
    }
}
