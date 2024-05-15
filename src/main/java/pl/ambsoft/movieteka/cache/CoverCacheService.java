package pl.ambsoft.movieteka.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.mapper.CoverMapper;
import pl.ambsoft.movieteka.model.dto.wrapper.CoversDto;
import pl.ambsoft.movieteka.model.entity.MovieEntity;
import pl.ambsoft.movieteka.repository.MovieRepository;

@Service
@RequiredArgsConstructor
public class CoverCacheService {

    private final MovieRepository movieRepository;

    private final CoverMapper coverMapper;

    @Cacheable(value = "coverByMovieId", key = "#movieId")
    public CoversDto getMovieCover(Long movieId) {
        var movieEntity = getMovieEntityById(movieId);
        return CoversDto.builder().covers(movieEntity.getCoverEntities().stream().map(coverMapper::toDto).toList()).build();
    }

    public MovieEntity getMovieEntityById(Long movieId) {
        return movieRepository.findById(movieId).orElseThrow(() -> new CustomErrorException("movie", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
    }
}
