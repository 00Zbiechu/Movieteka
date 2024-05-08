package pl.ambsoft.movieteka.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.ambsoft.movieteka.mapper.MovieMapper;
import pl.ambsoft.movieteka.model.dto.wrapper.MoviesDto;
import pl.ambsoft.movieteka.repository.MovieRepository;

@Service
@RequiredArgsConstructor
public class MovieCacheService {

    private final MovieRepository movieRepository;

    private final MovieMapper movieMapper;

    @Cacheable(value = "movies")
    public MoviesDto getAllMovies() {
        return MoviesDto.builder().movies(movieRepository.findAll().stream().map(movieMapper::toDto).toList()).build();
    }
}
