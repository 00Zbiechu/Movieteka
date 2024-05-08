package pl.ambsoft.movieteka.config;

import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

    @Override
    public void customize(ConcurrentMapCacheManager cacheManager) {
        cacheManager.setCacheNames(
                Lists.newArrayList(
                        "movies",
                        "moviesByCategory",
                        "moviesByTitle",
                        "ratingsByMovieId",
                        "categories",
                        "rewards",
                        "movieRewardsByMovieId"
                )
        );
    }
}
