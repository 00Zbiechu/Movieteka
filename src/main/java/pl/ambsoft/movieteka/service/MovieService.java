package pl.ambsoft.movieteka.service;

import org.springframework.web.multipart.MultipartFile;
import pl.ambsoft.movieteka.model.dto.MovieDto;
import pl.ambsoft.movieteka.model.dto.wrapper.MoviesDto;

public interface MovieService {

    MoviesDto getAllMovies();

    MoviesDto addNewMovie(MovieDto movieDto, MultipartFile photo);

    MoviesDto deleteMovie(Long id);

    MoviesDto editMovie(MovieDto editMovieDto);

    MoviesDto filterMovieByCategory(String category);

    MoviesDto searchMovieByTitle(String title);
}
