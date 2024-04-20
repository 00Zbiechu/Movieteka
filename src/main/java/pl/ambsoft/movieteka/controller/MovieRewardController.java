package pl.ambsoft.movieteka.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.ambsoft.movieteka.model.dto.wrapper.MovieRewardsDto;
import pl.ambsoft.movieteka.service.MovieRewardService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/movie-reward")
@RequiredArgsConstructor
public class MovieRewardController {

    private final MovieRewardService movieRewardService;

    @Operation(summary = "Get all rewards for movie by id")
    @GetMapping
    public ResponseEntity<MovieRewardsDto> getAllRewardsForMovie(@RequestParam Long movieId) {
        return new ResponseEntity<>(movieRewardService.getAllRewardsForMovie(movieId), HttpStatus.OK);
    }

    @Operation(summary = "Add reward to movie by passing movieId, rewardId and award received date")
    @PostMapping
    public ResponseEntity<MovieRewardsDto> addRewardToMovie(@RequestParam Long movieId, @RequestParam Long rewardId, @RequestParam LocalDate awardReceivedDate) {
        return new ResponseEntity<>(movieRewardService.addRewardToMovie(movieId, rewardId, awardReceivedDate), HttpStatus.CREATED);
    }

    @Operation(summary = "Remove reward form movie by passing movieId and rewardId")
    @DeleteMapping
    public ResponseEntity<MovieRewardsDto> removeRewardFromMovie(@RequestParam Long movieId, @RequestParam Long rewardId) {
        return new ResponseEntity<>(movieRewardService.removeRewardFromMovie(movieId, rewardId), HttpStatus.ACCEPTED);
    }
}
