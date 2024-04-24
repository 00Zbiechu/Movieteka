package pl.ambsoft.movieteka.service;

import org.springframework.web.multipart.MultipartFile;
import pl.ambsoft.movieteka.model.dto.AddMovieDto;
import pl.ambsoft.movieteka.model.dto.EditMovieDto;
import pl.ambsoft.movieteka.model.dto.wrapper.MoviesDto;

public interface MovieService {

    MoviesDto getAllMovies();

    MoviesDto addNewMovie(AddMovieDto addMovieDto, MultipartFile photo);

    MoviesDto deleteMovie(Long id);

    MoviesDto editMovie(EditMovieDto editMovieDto);

    MoviesDto filterMovieByCategory(String category);

    MoviesDto searchMovieByTitle(String title);
}
