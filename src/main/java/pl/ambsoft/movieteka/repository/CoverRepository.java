package pl.ambsoft.movieteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ambsoft.movieteka.model.entity.CoverEntity;
import pl.ambsoft.movieteka.model.entity.MovieEntity;

import java.util.List;

@Repository
public interface CoverRepository extends JpaRepository<CoverEntity, Long> {

    List<CoverEntity> findByMovieEntity(MovieEntity movieEntity);
}
