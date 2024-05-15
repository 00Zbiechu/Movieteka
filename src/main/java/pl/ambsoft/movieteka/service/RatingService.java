package pl.ambsoft.movieteka.service;

import pl.ambsoft.movieteka.model.dto.RatingDto;
import pl.ambsoft.movieteka.model.dto.wrapper.RatingsDto;

public interface RatingService {

    RatingsDto getMovieRatings(Long movieId);

    RatingsDto addMovieRating(Long movieId, RatingDto ratingDto);
}
