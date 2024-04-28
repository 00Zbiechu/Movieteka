package pl.ambsoft.movieteka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.mapper.RatingMapper;
import pl.ambsoft.movieteka.model.dto.RatingDto;
import pl.ambsoft.movieteka.model.dto.wrapper.RatingsDto;
import pl.ambsoft.movieteka.model.entity.MovieEntity;
import pl.ambsoft.movieteka.model.entity.RatingEntity;
import pl.ambsoft.movieteka.repository.MovieRepository;
import pl.ambsoft.movieteka.repository.RatingRepository;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final MovieRepository movieRepository;

    private final RatingMapper ratingMapper;

    private final RatingRepository ratingRepository;

    @Override
    public RatingsDto getMovieRatings(Long movieId) {
        MovieEntity movieEntity = getMovieEntityById(movieId);
        return RatingsDto.builder()
                .ratings(movieEntity.getRatingEntities().stream().map(ratingMapper::toDto).toList())
                .build();
    }

    @Override
    public RatingsDto addMovieRating(Long movieId, RatingDto ratingDto) {
        MovieEntity movieEntity = getMovieEntityById(movieId);
        RatingEntity ratingEntity = ratingMapper.toEntity(ratingDto);
        ratingEntity.setMovieEntity(movieEntity);
        ratingRepository.save(ratingEntity);
        return getMovieRatings(movieId);
    }

    private MovieEntity getMovieEntityById(Long movieId) {
        return movieRepository.findById(movieId).orElseThrow(() -> new CustomErrorException("movie", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
    }
}
