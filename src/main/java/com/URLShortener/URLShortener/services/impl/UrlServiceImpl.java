package com.URLShortener.URLShortener.services.impl;

import com.URLShortener.URLShortener.domain.CreateUrlRequest;
import com.URLShortener.URLShortener.domain.entities.Url;
import com.URLShortener.URLShortener.domain.entities.UrlClicks;
import com.URLShortener.URLShortener.exceptions.InvalidShortCode;
import com.URLShortener.URLShortener.exceptions.OriginalUrlIsNotUrl;
import com.URLShortener.URLShortener.repositories.cassandra.UrlClicksRepository;
import com.URLShortener.URLShortener.repositories.cassandra.UrlRepository;
import com.URLShortener.URLShortener.services.UrlService;
import org.hashids.Hashids;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;
    private final UrlClicksRepository urlClicksRepository;

    public UrlServiceImpl(UrlRepository urlRepository, UrlClicksRepository urlClicksRepository) {
        this.urlRepository = urlRepository;
        this.urlClicksRepository = urlClicksRepository;
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

        //long initialCount = urlRepository.count() + 985156648L; //change in prod

        // Generate unique ID using timestamp + random number to avoid count() query
        long uniqueId = System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(1000);

        //encrypt
        Hashids hashids = new Hashids("secret", 5); //change in prod
        String shortCode = hashids.encode(uniqueId);

        LocalDateTime now = LocalDateTime.now();
        Url newUrl = new Url(
                shortCode,
                request.getOriginalUrl(),
                now,
                now
        );

        urlRepository.save(newUrl);

        return newUrl;
    }

    @Override
    public String redirectToOriginalUrl(String shortCode) {

        if (shortCode == null || shortCode.isEmpty()) {
            throw new IllegalArgumentException("Original URL cannot be null or empty");
        }

        Url url = urlRepository.findById(shortCode)
                .orElseThrow(InvalidShortCode::new);

        return url.getOriginalUrl();
    }
}
