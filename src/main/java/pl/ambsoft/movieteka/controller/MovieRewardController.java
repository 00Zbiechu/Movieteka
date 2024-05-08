package pl.ambsoft.movieteka.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.ambsoft.movieteka.cache.MovieRewardCacheService;
import pl.ambsoft.movieteka.exception.wrapper.ErrorList;
import pl.ambsoft.movieteka.model.dto.wrapper.MovieRewardsDto;
import pl.ambsoft.movieteka.model.dto.wrapper.RewardsDto;
import pl.ambsoft.movieteka.service.MovieRewardService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/movie-reward")
@Tag(name = "Movie Reward API")
@RequiredArgsConstructor
public class MovieRewardController {

    private final MovieRewardService movieRewardService;

    private final MovieRewardCacheService movieRewardCacheService;

    @Operation(summary = "Get all rewards for movie by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all rewards of movie",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RewardsDto.class))})
    })
    @GetMapping
    public ResponseEntity<MovieRewardsDto> getAllRewardsForMovie(@RequestParam @Parameter(description = "Movie ID", example = "1") Long movieId) {
        return new ResponseEntity<>(movieRewardCacheService.getAllRewardsForMovie(movieId), HttpStatus.OK);
    }

    @Operation(summary = "Add reward to movie by passing movieId, rewardId and award received date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns all rewards of movie",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RewardsDto.class))}),
            @ApiResponse(responseCode = "404", description = "Movie does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))}),
            @ApiResponse(responseCode = "404", description = "Reward does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @PostMapping
    public ResponseEntity<MovieRewardsDto> addRewardToMovie(@RequestParam @Parameter(description = "Movie ID", example = "1") Long movieId,
                                                            @RequestParam @Parameter(description = "Reward ID", example = "1") Long rewardId,
                                                            @RequestParam @Parameter(description = "Award Received Date", example = "2024-02-02") LocalDate awardReceivedDate) {
        return new ResponseEntity<>(movieRewardService.addRewardToMovie(movieId, rewardId, awardReceivedDate), HttpStatus.CREATED);
    }

    @Operation(summary = "Remove reward form movie by passing movieId and rewardId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all rewards of movie",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RewardsDto.class))}),
            @ApiResponse(responseCode = "404", description = "Movie does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))}),
            @ApiResponse(responseCode = "404", description = "Reward does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))}),
            @ApiResponse(responseCode = "404", description = "MovieReward does not exist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @DeleteMapping
    public ResponseEntity<MovieRewardsDto> removeRewardFromMovie(@RequestParam @Parameter(description = "Movie ID", example = "1") Long movieId,
                                                                 @RequestParam @Parameter(description = "Reward ID", example = "1") Long rewardId) {
        return new ResponseEntity<>(movieRewardService.removeRewardFromMovie(movieId, rewardId), HttpStatus.OK);
    }
}
