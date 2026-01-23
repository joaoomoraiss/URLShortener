package com.URLShortener.URLShortener.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "urls")
public class Url {

    @Id
    @Column(name = "short_code", updatable = false, nullable = false, length = 7)
    @NotNull
    private String shortCode; // base 62

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "access_count", nullable = false)
    private Long accessCount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Url() {
    }

    public Url(String shortCode, String originalUrl, Long accessCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.accessCount = accessCount;
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

    public Long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(Long accessCount) {
        this.accessCount = accessCount;
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
        return Objects.equals(shortCode, url.shortCode) && Objects.equals(originalUrl, url.originalUrl) && Objects.equals(accessCount, url.accessCount) && Objects.equals(createdAt, url.createdAt) && Objects.equals(updatedAt, url.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortCode, originalUrl, accessCount, createdAt, updatedAt);
    }
}
