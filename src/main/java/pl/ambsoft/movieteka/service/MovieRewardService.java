package pl.ambsoft.movieteka.service;

import pl.ambsoft.movieteka.model.dto.wrapper.MovieRewardsDto;

import java.time.LocalDate;

public interface MovieRewardService {

    MovieRewardsDto getAllRewardsForMovie(Long movieId);

    MovieRewardsDto addRewardToMovie(Long movieId, Long rewardId, LocalDate awardReceivedDate);

    MovieRewardsDto removeRewardFromMovie(Long movieId, Long rewardId);
}
