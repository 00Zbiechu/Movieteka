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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.ambsoft.movieteka.exception.wrapper.ErrorList;
import pl.ambsoft.movieteka.model.dto.RatingDto;
import pl.ambsoft.movieteka.model.dto.wrapper.RatingsDto;
import pl.ambsoft.movieteka.service.RatingService;

@RestController
@RequestMapping("/api/rating")
@Tag(name = "Rating API")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @Operation(summary = "Get list of all ratings by movie id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return list of ratings for movie by id",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RatingsDto.class))}),
            @ApiResponse(responseCode = "400", description = "Return error list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @GetMapping
    public ResponseEntity<RatingsDto> getMovieRatings(@RequestParam @Parameter(description = "Movie ID", example = "1") Long movieId) {
        return new ResponseEntity<>(ratingService.getMovieRatings(movieId), HttpStatus.OK);
    }

    @Operation(summary = "Add new movie rating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return list of ratings for movie by id",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RatingsDto.class))}),
            @ApiResponse(responseCode = "400", description = "Return error list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @PostMapping
    public ResponseEntity<RatingsDto> addMovieRating(@RequestParam @Parameter(description = "Movie ID", example = "1") Long movieId,
                                                     @Valid @RequestBody RatingDto ratingDto) {
        return new ResponseEntity<>(ratingService.addMovieRating(movieId, ratingDto), HttpStatus.CREATED);
    }
}
