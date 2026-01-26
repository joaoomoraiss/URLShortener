package com.URLShortener.URLShortener.domain.entities;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Table("urls")
public class Url {

    @PrimaryKey
    @Column("short_code")
    private String shortCode; // base 62

    @Column("original_url")
    private String originalUrl;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    public Url() {
    }

    public Url(String shortCode, String originalUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Url url = (Url) o;
        return Objects.equals(shortCode, url.shortCode) && Objects.equals(originalUrl, url.originalUrl) && Objects.equals(createdAt, url.createdAt) && Objects.equals(updatedAt, url.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortCode, originalUrl, createdAt, updatedAt);
    }
}
