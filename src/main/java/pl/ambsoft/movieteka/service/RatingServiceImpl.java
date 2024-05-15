package pl.ambsoft.movieteka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import pl.ambsoft.movieteka.cache.RatingCacheService;
import pl.ambsoft.movieteka.mapper.RatingMapper;
import pl.ambsoft.movieteka.model.dto.RatingDto;
import pl.ambsoft.movieteka.model.dto.wrapper.RatingsDto;
import pl.ambsoft.movieteka.model.entity.MovieEntity;
import pl.ambsoft.movieteka.model.entity.RatingEntity;
import pl.ambsoft.movieteka.repository.RatingRepository;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingMapper ratingMapper;

    private final RatingRepository ratingRepository;

    private final RatingCacheService ratingCacheService;

    @Override
    public RatingsDto getMovieRatings(Long movieId) {
        return ratingCacheService.getMovieRatings(movieId);
    }

    @CacheEvict(cacheNames = "ratingsByMovieId", allEntries = true, beforeInvocation = true)
    @Override
    public RatingsDto addMovieRating(Long movieId, RatingDto ratingDto) {
        MovieEntity movieEntity = ratingCacheService.getMovieEntityById(movieId);
        RatingEntity ratingEntity = ratingMapper.toEntity(ratingDto);
        ratingEntity.setMovieEntity(movieEntity);
        ratingRepository.save(ratingEntity);
        return getMovieRatings(movieId);
    }
}
