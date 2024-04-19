package pl.ambsoft.movieteka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.ambsoft.movieteka.model.entity.MovieEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {

    Optional<MovieEntity> findByTitle(String title);

    @Query("SELECT m FROM MovieEntity m JOIN m.categoryEntities c WHERE c.name=:category")
    List<MovieEntity> findByCategory(String category);
}
