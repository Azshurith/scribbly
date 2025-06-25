package com.devitrax.scribbly.integration;

import com.devitrax.scribbly.ScribblyApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ScribblyApplication.class)
class ScribblyApplicationTest {

	@Autowired
	private CacheManager cacheManager;

	@Test
	void contextLoads() {
		// If context fails to load, this test will fail automatically
	}

	@Test
	void cachingShouldBeEnabled() {
		assertThat(cacheManager).isNotNull();

		cacheManager.getCache("postsPage"); // triggers cache creation
		assertThat(cacheManager.getCacheNames()).contains("postsPage");
	}

}
