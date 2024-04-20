package pl.ambsoft.movieteka.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.ambsoft.movieteka.model.dto.MovieDto;
import pl.ambsoft.movieteka.model.dto.wrapper.MoviesDto;
import pl.ambsoft.movieteka.service.MovieService;
import pl.ambsoft.movieteka.validatior.MovieValidator;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieValidator movieValidator;

    @InitBinder("movieDto")
    public void validateMovie(WebDataBinder binder) {
        binder.addValidators(movieValidator);
    }

    private final MovieService movieService;

    @Operation(summary = "Get all movies")
    @GetMapping
    public ResponseEntity<MoviesDto> getAllMovies() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @Operation(summary = "Add new movie with optional photo")
    @PostMapping
    public ResponseEntity<MoviesDto> addNewMovie(@RequestPart(required = false) MultipartFile photo, @Valid @RequestPart MovieDto movieDto) {
        return new ResponseEntity<>(movieService.addNewMovie(movieDto, photo), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete movie by id")
    @DeleteMapping
    public ResponseEntity<MoviesDto> deleteMovie(@RequestParam Long id) {
        return new ResponseEntity<>(movieService.deleteMovie(id), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Edit existing movie data")
    @PutMapping
    public ResponseEntity<MoviesDto> editMovie(@Valid @RequestBody MovieDto editMovieDto) {
        return new ResponseEntity<>(movieService.editMovie(editMovieDto), HttpStatus.OK);
    }

    @Operation(summary = "Filter movies by category")
    @GetMapping("/filter")
    public ResponseEntity<MoviesDto> filterMovieByCategory(@RequestParam String category) {
        return new ResponseEntity<>(movieService.filterMovieByCategory(category), HttpStatus.OK);
    }

    @Operation(summary = "Search for movie by title")
    @GetMapping("/search")
    public ResponseEntity<MoviesDto> searchMovieByTitle(@RequestParam String title) {
        return new ResponseEntity<>(movieService.searchMovieByTitle(title), HttpStatus.OK);
    }
}
