package pl.ambsoft.movieteka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.ambsoft.movieteka.cache.RewardCacheService;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.mapper.RewardMapper;
import pl.ambsoft.movieteka.model.dto.RewardDto;
import pl.ambsoft.movieteka.model.dto.wrapper.RewardsDto;
import pl.ambsoft.movieteka.repository.RewardRepository;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final RewardRepository rewardRepository;

    private final RewardMapper rewardMapper;

    private final RewardCacheService rewardCacheService;

    @CacheEvict(cacheNames = "rewards", allEntries = true, beforeInvocation = true)
    @Override
    public RewardsDto addNewRewards(RewardsDto rewardsDto) {
        for (RewardDto rewardDto : rewardsDto.rewards()) {
            rewardRepository.save(rewardMapper.toEntity(rewardDto));
        }
        return rewardCacheService.getRewards();
    }

    @CacheEvict(cacheNames = "rewards", allEntries = true, beforeInvocation = true)
    @Override
    public RewardsDto deleteReward(Long id) {
        var rewardEntity = rewardRepository.findById(id).orElseThrow(
                () -> new CustomErrorException("reward", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND)
        );
        rewardRepository.delete(rewardEntity);
        return rewardCacheService.getRewards();
    }
}
