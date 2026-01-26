package com.URLShortener.URLShortener.repositories.cassandra;

import com.URLShortener.URLShortener.domain.entities.UrlClicks;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlClicksRepository extends CassandraRepository<UrlClicks, String> {

    @Query("UPDATE url_clicks SET clicks = clicks + 1 WHERE short_code = ?0")
    void increment(String shortCode);

    @Query("SELECT clicks FROM url_clicks WHERE short_code = ?0")
    Long findClicksByShortCode(String shortCode);
}
