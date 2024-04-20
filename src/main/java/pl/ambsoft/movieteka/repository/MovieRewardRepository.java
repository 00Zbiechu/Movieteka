package pl.ambsoft.movieteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.ambsoft.movieteka.model.entity.MovieRewardEntity;
import pl.ambsoft.movieteka.model.entity.key.MovieRewardKey;

import java.util.List;

@Repository
public interface MovieRewardRepository extends JpaRepository<MovieRewardEntity, MovieRewardKey> {

    @Query("SELECT mr FROM MovieRewardEntity mr JOIN mr.movieRewardKey.movieEntity m WHERE m.id=:movieId")
    List<MovieRewardEntity> findAllByMovieId(Long movieId);
}
