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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.ambsoft.movieteka.exception.wrapper.ErrorList;
import pl.ambsoft.movieteka.model.dto.AddMovieDto;
import pl.ambsoft.movieteka.model.dto.EditMovieDto;
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

    @InitBinder("addMovieDto")
    public void validateAddMovie(WebDataBinder binder) {
        binder.addValidators(addMovieValidator);
    }

    @InitBinder("editMovieDto")
    public void validateEditMovie(WebDataBinder binder) {
        binder.addValidators(editMovieValidator);
    }

    private final MovieService movieService;

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

    @Operation(summary = "Add new movie with optional photo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return list of all movies",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MoviesDto.class))}),
            @ApiResponse(responseCode = "400", description = "Return error list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MoviesDto> addNewMovie(
            @RequestPart(required = false) MultipartFile photo,
            @Valid @RequestPart AddMovieDto addMovieDto) {
        return new ResponseEntity<>(movieService.addNewMovie(addMovieDto, photo), HttpStatus.CREATED);
    }

    @Operation(summary = "Edit existing movie data with optional photo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return list of all movies",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MoviesDto.class))}),
            @ApiResponse(responseCode = "400", description = "Return error list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MoviesDto> editMovie(
            @RequestPart(required = false) MultipartFile photo,
            @Valid @RequestPart EditMovieDto editMovieDto) {
        return new ResponseEntity<>(movieService.editMovie(editMovieDto, photo), HttpStatus.OK);
    }

    @Operation(summary = "Delete movie by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Return list of all movies",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MoviesDto.class))}),
            @ApiResponse(responseCode = "400", description = "Return error list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @DeleteMapping
    public ResponseEntity<MoviesDto> deleteMovie(@RequestParam @Parameter(description = "Movie ID", example = "1") Long id) {
        return new ResponseEntity<>(movieService.deleteMovie(id), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Filter movies by category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a list of movies in a given category",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MoviesDto.class))}),
            @ApiResponse(responseCode = "400", description = "Return error list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @GetMapping("/filter")
    public ResponseEntity<MoviesDto> filterMovieByCategory(@RequestParam @Parameter(description = "Category name", example = "drama") String category) {
        return new ResponseEntity<>(movieService.filterMovieByCategory(category), HttpStatus.OK);
    }

    @Operation(summary = "Search for movie by title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a list of movies which contains pass phrase in title",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MoviesDto.class))}),
            @ApiResponse(responseCode = "400", description = "Return error list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @GetMapping("/search")
    public ResponseEntity<MoviesDto> searchMovieByTitle(@RequestParam @Parameter(description = "Movie title", example = "Mad Max") String title) {
        return new ResponseEntity<>(movieService.searchMovieByTitle(title), HttpStatus.OK);
    }
}
