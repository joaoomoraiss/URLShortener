package com.URLShortener.URLShortener.repositories.jpa;

import com.URLShortener.URLShortener.domain.entities.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url, String>{
}
