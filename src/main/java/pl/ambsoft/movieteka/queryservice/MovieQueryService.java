package pl.ambsoft.movieteka.queryservice;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.ambsoft.movieteka.model.entity.MovieEntity;
import pl.ambsoft.movieteka.model.entity.QMovieEntity;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MovieQueryService {

    private final JPAQueryFactory queryFactory;
    private static final QMovieEntity movie = QMovieEntity.movieEntity;

    public List<MovieEntity> getMovieEntityByTitle(String title) {
        return queryFactory.selectFrom(movie).where(movie.title.contains(title)).fetch();
    }
}
