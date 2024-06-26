package pl.ambsoft.movieteka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.ambsoft.movieteka.cache.MovieRewardCacheService;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.model.dto.wrapper.MovieRewardsDto;
import pl.ambsoft.movieteka.model.entity.MovieRewardEntity;
import pl.ambsoft.movieteka.model.entity.key.MovieRewardKey;
import pl.ambsoft.movieteka.repository.MovieRepository;
import pl.ambsoft.movieteka.repository.MovieRewardRepository;
import pl.ambsoft.movieteka.repository.RewardRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MovieRewardServiceImpl implements MovieRewardService {

    private final MovieRewardRepository movieRewardRepository;

    private final MovieRepository movieRepository;

    private final RewardRepository rewardRepository;

    private final MovieRewardCacheService movieRewardCacheService;

    @Override
    public MovieRewardsDto getAllRewardsForMovie(Long movieId) {
        return movieRewardCacheService.getAllRewardsForMovie(movieId);
    }

    @CacheEvict(value = "movieRewardsByMovieId", allEntries = true, beforeInvocation = true)
    @Override
    public MovieRewardsDto addRewardToMovie(Long movieId, Long rewardId, LocalDate awardReceivedDate) {
        var movieRewardEntity = MovieRewardEntity.builder()
                .movieRewardKey(MovieRewardKey.builder()
                        .movieEntity(movieRepository.findById(movieId).orElseThrow(() -> new CustomErrorException("movie", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND)))
                        .rewardEntity(rewardRepository.findById(rewardId).orElseThrow(() -> new CustomErrorException("reward", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND)))
                        .build()
                )
                .awardReceivedDate(awardReceivedDate)
                .build();

        movieRewardRepository.findById(movieRewardEntity.getMovieRewardKey()).ifPresent(
                movieReward -> {
                    throw new CustomErrorException("movieReward", ErrorCodes.ENTITY_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
                }
        );
        movieRewardRepository.save(movieRewardEntity);
        return getAllRewardsForMovie(movieId);
    }

    @CacheEvict(value = "movieRewardsByMovieId", allEntries = true, beforeInvocation = true)
    @Override
    public MovieRewardsDto removeRewardFromMovie(Long movieId, Long rewardId) {
        var movieRewardEntity = movieRewardRepository.findById(MovieRewardKey.builder()
                .movieEntity(movieRepository.findById(movieId).orElseThrow(() -> new CustomErrorException("movie", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.BAD_REQUEST)))
                .rewardEntity(rewardRepository.findById(rewardId).orElseThrow(() -> new CustomErrorException("reward", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND)))
                .build()).orElseThrow(() -> new CustomErrorException("movieReward", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
        movieRewardRepository.delete(movieRewardEntity);
        return getAllRewardsForMovie(movieId);
    }
}
