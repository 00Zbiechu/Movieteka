package pl.ambsoft.movieteka.service;

import java.time.LocalDate;

public interface MovieRewardService {

    void addRewardToMovie(Long movieId, Long rewardId, LocalDate awardReceivedDate);

    void removeRewardFromMovie(Long movieId, Long rewardId);
}
