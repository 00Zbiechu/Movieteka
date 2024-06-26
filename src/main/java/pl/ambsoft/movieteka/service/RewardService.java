package pl.ambsoft.movieteka.service;

import pl.ambsoft.movieteka.model.dto.wrapper.RewardsDto;

public interface RewardService {

    RewardsDto getRewards();

    RewardsDto addNewRewards(RewardsDto rewardsDto);

    RewardsDto deleteReward(Long id);
}
