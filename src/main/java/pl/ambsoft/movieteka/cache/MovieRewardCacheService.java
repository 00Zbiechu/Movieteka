package pl.ambsoft.movieteka.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.ambsoft.movieteka.model.dto.MovieRewardDto;
import pl.ambsoft.movieteka.model.dto.wrapper.MovieRewardsDto;
import pl.ambsoft.movieteka.model.entity.MovieRewardEntity;
import pl.ambsoft.movieteka.repository.MovieRewardRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieRewardCacheService {

    private final MovieRewardRepository movieRewardRepository;

    @Cacheable(value = "movieRewardsByMovieId", key = "#movieId")
    public MovieRewardsDto getAllRewardsForMovie(Long movieId) {
        List<MovieRewardEntity> movieRewardEntities = movieRewardRepository.findAllByMovieId(movieId);
        return MovieRewardsDto.builder().movieRewards(movieRewardEntities.stream()
                        .map(movieReward -> MovieRewardDto.builder()
                                .id(movieReward.getMovieRewardKey().getRewardEntity().getId())
                                .name(movieReward.getMovieRewardKey().getRewardEntity().getName())
                                .awardReceivedDate(movieReward.getAwardReceivedDate())
                                .build()).toList())
                .build();
    }
}
