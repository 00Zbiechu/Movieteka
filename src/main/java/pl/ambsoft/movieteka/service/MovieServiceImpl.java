package pl.ambsoft.movieteka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.ambsoft.movieteka.cache.MovieCacheService;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.mapper.MovieMapper;
import pl.ambsoft.movieteka.model.dto.AddMovieDto;
import pl.ambsoft.movieteka.model.dto.MovieDto;
import pl.ambsoft.movieteka.model.dto.wrapper.MoviesDto;
import pl.ambsoft.movieteka.queryservice.MovieQueryService;
import pl.ambsoft.movieteka.repository.MovieRepository;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    private final MovieMapper movieMapper;

    private final MovieQueryService movieQueryService;

    private final MovieCacheService movieCacheService;

    @Override
    public MoviesDto getAllMovies() {
        return movieCacheService.getAllMovies();
    }

    @CacheEvict(cacheNames = {"movies", "moviesByCategory", "moviesByTitle"}, allEntries = true, beforeInvocation = true)
    @Override
    public MoviesDto addNewMovie(AddMovieDto addMovieDto) {
        var movieEntity = movieMapper.toEntity(addMovieDto);
        movieRepository.save(movieEntity);
        return getAllMovies();
    }

    @CacheEvict(cacheNames = {"movies", "moviesByCategory", "moviesByTitle"}, allEntries = true, beforeInvocation = true)
    @Override
    public MoviesDto editMovie(MovieDto movieDto) {
        var movieEntity = movieRepository.findById(movieDto.getId()).orElseThrow(() -> new CustomErrorException("movie", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
        movieMapper.updateMovieEntityWithEditMovieDto(movieDto, movieEntity);
        movieRepository.save(movieEntity);
        return getAllMovies();
    }

    @Override
    public MoviesDto deleteMovie(Long id) {
        var movieEntity = movieRepository.findById(id).orElseThrow(() -> new CustomErrorException("movie", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
        movieRepository.delete(movieEntity);
        return getAllMovies();
    }

    @Cacheable(cacheNames = "moviesByCategory", key = "#category")
    @Override
    public MoviesDto filterMovieByCategory(String category) {
        return MoviesDto.builder().movies(movieRepository.findByCategory(category).stream().map(movieMapper::toDto).toList()).build();
    }

    @Cacheable(cacheNames = "moviesByTitle", key = "#title")
    @Override
    public MoviesDto searchMovieByTitle(String title) {
        return MoviesDto.builder().movies(movieQueryService.getMovieEntityByTitle(title).stream().map(movieMapper::toDto).toList()).build();
    }
}
