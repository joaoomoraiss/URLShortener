package com.URLShortener.URLShortener;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
	"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration"
})
@ActiveProfiles("test")
class UrlShortenerApplicationTests {

	@Test
	void contextLoads() {
	}

}
