package pl.ambsoft.movieteka.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.ambsoft.movieteka.mapper.RewardMapper;
import pl.ambsoft.movieteka.model.dto.wrapper.RewardsDto;
import pl.ambsoft.movieteka.repository.RewardRepository;

@Service
@RequiredArgsConstructor
public class RewardCacheService {

    private final RewardMapper rewardMapper;

    private final RewardRepository rewardRepository;

    @Cacheable(value = "rewards")
    public RewardsDto getRewards() {
        return RewardsDto.builder().rewards(rewardRepository.findAll().stream().map(rewardMapper::toDto).toList()).build();
    }
}
