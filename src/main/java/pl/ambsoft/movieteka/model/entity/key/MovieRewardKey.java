package pl.ambsoft.movieteka.model.entity.key;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import pl.ambsoft.movieteka.model.entity.MovieEntity;
import pl.ambsoft.movieteka.model.entity.RewardEntity;

@Embeddable
@Data
public class MovieRewardKey {

    @ManyToOne
    @JoinColumn(name = "movie_id", referencedColumnName = "id")
    private MovieEntity movieEntity;

    @ManyToOne
    @JoinColumn(name = "reward_id", referencedColumnName = "id")
    private RewardEntity rewardEntity;
}
