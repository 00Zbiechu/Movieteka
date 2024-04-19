package pl.ambsoft.movieteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ambsoft.movieteka.model.entity.MovieRewardEntity;
import pl.ambsoft.movieteka.model.entity.key.MovieRewardKey;

@Repository
public interface MovieRewardRepository extends JpaRepository<MovieRewardEntity, MovieRewardKey> {
}
