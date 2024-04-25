package pl.ambsoft.movieteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ambsoft.movieteka.model.entity.RatingEntity;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
}
