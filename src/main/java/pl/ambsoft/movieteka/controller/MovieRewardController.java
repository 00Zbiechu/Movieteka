package pl.ambsoft.movieteka.controller;

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

    @GetMapping
    public ResponseEntity<MovieRewardsDto> getAllRewardsForMovie(@RequestParam Long movieId) {
        return new ResponseEntity<>(movieRewardService.getAllRewardsForMovie(movieId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> addRewardToMovie(@RequestParam Long movieId, @RequestParam Long rewardId, @RequestParam LocalDate awardReceivedDate) {
        movieRewardService.addRewardToMovie(movieId, rewardId, awardReceivedDate);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeRewardFromMovie(@RequestParam Long movieId, @RequestParam Long rewardId) {
        movieRewardService.removeRewardFromMovie(movieId, rewardId);
        return ResponseEntity.ok().build();
    }
}
