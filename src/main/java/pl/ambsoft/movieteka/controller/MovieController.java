package pl.ambsoft.movieteka.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RestController;
import pl.ambsoft.movieteka.exception.wrapper.ErrorList;
import pl.ambsoft.movieteka.model.dto.AddMovieDto;
import pl.ambsoft.movieteka.model.dto.MovieDto;
import pl.ambsoft.movieteka.model.dto.wrapper.MoviesDto;
import pl.ambsoft.movieteka.service.MovieService;
import pl.ambsoft.movieteka.validator.AddMovieValidator;
import pl.ambsoft.movieteka.validator.EditMovieValidator;

@RestController
@RequestMapping("/api/movie")
@Tag(name = "Movie API")
@RequiredArgsConstructor
public class MovieController {

    private final AddMovieValidator addMovieValidator;

    private final EditMovieValidator editMovieValidator;

    private final MovieService movieService;

    @InitBinder("addMovieDto")
    public void validateAddMovie(WebDataBinder binder) {
        binder.addValidators(addMovieValidator);
    }

    @InitBinder("editMovieDto")
    public void validateEditMovie(WebDataBinder binder) {
        binder.addValidators(editMovieValidator);
    }

    @Operation(summary = "Get all movies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return list of all movies",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MoviesDto.class))})
    })
    @GetMapping
    public ResponseEntity<MoviesDto> getAllMovies() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @Operation(summary = "Add new movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return list of all movies",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MoviesDto.class))}),
            @ApiResponse(responseCode = "400", description = "Return error list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))}),
            @ApiResponse(responseCode = "404", description = "Category does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @PostMapping
    public ResponseEntity<MoviesDto> addNewMovie(@Valid @RequestBody AddMovieDto addMovieDto) {
        return new ResponseEntity<>(movieService.addNewMovie(addMovieDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Edit existing movie data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return list of all movies",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MoviesDto.class))}),
            @ApiResponse(responseCode = "400", description = "Return error list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))}),
            @ApiResponse(responseCode = "404", description = "Movie does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))}),
            @ApiResponse(responseCode = "404", description = "Category does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @PutMapping
    public ResponseEntity<MoviesDto> editMovie(@Valid @RequestBody MovieDto movieDto) {
        return new ResponseEntity<>(movieService.editMovie(movieDto), HttpStatus.OK);
    }

    @Operation(summary = "Delete movie by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return list of all movies",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MoviesDto.class))}),
            @ApiResponse(responseCode = "404", description = "Movie does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @DeleteMapping
    public ResponseEntity<MoviesDto> deleteMovie(@RequestParam @Parameter(description = "Movie ID", example = "1") Long id) {
        return new ResponseEntity<>(movieService.deleteMovie(id), HttpStatus.OK);
    }

    @Operation(summary = "Filter movies by category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a list of movies in a given category",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MoviesDto.class))})
    })
    @GetMapping("/filter")
    public ResponseEntity<MoviesDto> filterMovieByCategory(@RequestParam @Parameter(description = "Category name", example = "drama") String category) {
        return new ResponseEntity<>(movieService.filterMovieByCategory(category), HttpStatus.OK);
    }

    @Operation(summary = "Search for movie by title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a list of movies which contains pass phrase in title",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MoviesDto.class))})
    })
    @GetMapping("/search")
    public ResponseEntity<MoviesDto> searchMovieByTitle(@RequestParam @Parameter(description = "Movie title", example = "Mad Max") String title) {
        return new ResponseEntity<>(movieService.searchMovieByTitle(title), HttpStatus.OK);
    }
}
