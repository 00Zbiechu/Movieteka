package pl.ambsoft.movieteka.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.mapper.RatingMapper;
import pl.ambsoft.movieteka.model.dto.wrapper.RatingsDto;
import pl.ambsoft.movieteka.model.entity.MovieEntity;
import pl.ambsoft.movieteka.repository.MovieRepository;

@Service
@RequiredArgsConstructor
public class RatingCacheService {

    private final RatingMapper ratingMapper;

    private final MovieRepository movieRepository;

    @Cacheable(value = "ratingsByMovieId", key = "#movieId")
    public RatingsDto getMovieRatings(Long movieId) {
        MovieEntity movieEntity = getMovieEntityById(movieId);
        return RatingsDto.builder()
                .ratings(movieEntity.getRatingEntities().stream().map(ratingMapper::toDto).toList())
                .build();
    }

    public MovieEntity getMovieEntityById(Long movieId) {
        return movieRepository.findById(movieId).orElseThrow(() -> new CustomErrorException("movie", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
    }
}
