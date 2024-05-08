package pl.ambsoft.movieteka.service;

import pl.ambsoft.movieteka.model.dto.wrapper.RewardsDto;

public interface RewardService {

    RewardsDto addNewRewards(RewardsDto rewardsDto);

    RewardsDto deleteReward(Long id);
}
