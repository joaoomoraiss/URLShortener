package com.URLShortener.URLShortener.domain.entities;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("url_clicks")
public class UrlClicks {

    @PrimaryKey
    @Column("short_code")
    private String shortCode;

    @Column("clicks")
    private Long clicks;

    public UrlClicks() {
    }

    public UrlClicks(String shortCode, Long clicks) {
        this.shortCode = shortCode;
        this.clicks = clicks;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public Long getClicks() {
        return clicks;
    }

    public void setClicks(Long clicks) {
        this.clicks = clicks;
    }
}
