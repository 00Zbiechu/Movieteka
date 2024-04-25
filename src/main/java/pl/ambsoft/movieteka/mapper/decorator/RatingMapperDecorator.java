package pl.ambsoft.movieteka.mapper.decorator;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import pl.ambsoft.movieteka.mapper.RatingMapper;
import pl.ambsoft.movieteka.model.dto.RatingDto;
import pl.ambsoft.movieteka.model.entity.RatingEntity;

@NoArgsConstructor
public abstract class RatingMapperDecorator implements RatingMapper {

    @Autowired
    private RatingMapper ratingMapper;

    @Override
    public RatingEntity toEntity(RatingDto dto) {
        var ratingEntity = ratingMapper.toEntity(dto);
        ratingEntity.setScore(Math.round(dto.score() * 10.0f) / 10.0f);
        return ratingEntity;
    }
}
