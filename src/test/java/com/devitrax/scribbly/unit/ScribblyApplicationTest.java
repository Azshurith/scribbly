package com.devitrax.scribbly.unit;

import com.devitrax.scribbly.ScribblyApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ScribblyApplicationTest {

    @Autowired
    private CacheManager cacheManager;

    @Test
    void cachingShouldBeEnabled() {
        assertThat(cacheManager).isNotNull();
    }

    @Test
    void mainMethodShouldRunWithoutErrors() {
        ScribblyApplication.main(new String[] {});
    }

}
