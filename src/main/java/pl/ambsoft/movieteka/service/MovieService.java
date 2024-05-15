package pl.ambsoft.movieteka.service;

import pl.ambsoft.movieteka.model.dto.AddMovieDto;
import pl.ambsoft.movieteka.model.dto.MovieDto;
import pl.ambsoft.movieteka.model.dto.wrapper.MoviesDto;

public interface MovieService {

    MoviesDto getAllMovies();

    MoviesDto addNewMovie(AddMovieDto addMovieDto);

    MoviesDto editMovie(MovieDto movieDto);

    MoviesDto deleteMovie(Long id);

    MoviesDto filterMovieByCategory(String category);

    MoviesDto searchMovieByTitle(String title);
}
