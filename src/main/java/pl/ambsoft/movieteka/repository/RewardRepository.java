package pl.ambsoft.movieteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ambsoft.movieteka.model.entity.RewardEntity;

import java.util.Optional;

@Repository
public interface RewardRepository extends JpaRepository<RewardEntity, Long> {

    Optional<RewardEntity> findByName(String name);
}
