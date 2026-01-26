package com.URLShortener.URLShortener.repositories.cassandra;

import com.URLShortener.URLShortener.domain.entities.Url;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends CassandraRepository<Url, String> {
}
