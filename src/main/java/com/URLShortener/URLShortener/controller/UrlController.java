package com.URLShortener.URLShortener.controller;

import com.URLShortener.URLShortener.domain.CreateUrlRequest;
import com.URLShortener.URLShortener.domain.RedirectUrlRequest;
import com.URLShortener.URLShortener.domain.dto.UrlDto;
import com.URLShortener.URLShortener.domain.entities.Url;
import com.URLShortener.URLShortener.mappers.UrlMapper;
import com.URLShortener.URLShortener.services.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/url")
@CrossOrigin(origins = "http://localhost:4200")

public class UrlController {

    private final UrlService urlService;
    private final UrlMapper urlMapper;

    public UrlController(UrlService urlService, UrlMapper urlMapper) {
        this.urlService = urlService;
        this.urlMapper = urlMapper;
    }

    @PostMapping()
    public ResponseEntity<UrlDto> createUrl(@RequestBody CreateUrlRequest request) {
        Url shortUrl = urlService.createShortUrl(request);
        UrlDto urlDto = urlMapper.toDto(shortUrl);
        return ResponseEntity.ok(urlDto);
    }

    @GetMapping(path = "/{shortCode}")
    public ResponseEntity<String> redirectToOriginalUrl(@PathVariable("shortCode") String shortCode) {
        String originalUrl = urlService.redirectToOriginalUrl(shortCode);
        return ResponseEntity.ok(originalUrl);
    }
}
