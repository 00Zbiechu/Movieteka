package pl.ambsoft.movieteka.mapper.decorator;

import com.google.common.collect.Sets;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.mapper.CategoryMapper;
import pl.ambsoft.movieteka.mapper.MovieMapper;
import pl.ambsoft.movieteka.model.dto.CategoryDto;
import pl.ambsoft.movieteka.model.dto.MovieDto;
import pl.ambsoft.movieteka.model.dto.RewardDto;
import pl.ambsoft.movieteka.model.dto.wrapper.CategoriesDto;
import pl.ambsoft.movieteka.model.entity.CategoryEntity;
import pl.ambsoft.movieteka.model.entity.MovieEntity;
import pl.ambsoft.movieteka.model.entity.MovieRewardEntity;
import pl.ambsoft.movieteka.model.entity.key.MovieRewardKey;
import pl.ambsoft.movieteka.repository.CategoryRepository;
import pl.ambsoft.movieteka.repository.RewardRepository;

import java.util.Set;

@NoArgsConstructor
public class MovieMapperDecorator implements MovieMapper {

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RewardRepository rewardRepository;

    @Override
    public MovieEntity toEntity(MovieDto dto) {
        var movieEntity = movieMapper.toEntity(dto);
        setCategoriesForMovieEntity(dto, movieEntity);
        setRewardsForMovieEntity(dto, movieEntity);
        return movieEntity;
    }

    private void setCategoriesForMovieEntity(MovieDto dto, MovieEntity movieEntity) {
        Set<CategoryEntity> categoriesSet = Sets.newHashSet();
        for (CategoryDto categoryDto : dto.getCategoriesDto().getCategories()) {
            categoriesSet.add(categoryRepository.findByName(categoryDto.name()).orElseThrow(() -> new CustomErrorException("category", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.BAD_REQUEST)));
        }
        movieEntity.setCategoryEntities(categoriesSet);
    }

    private void setRewardsForMovieEntity(MovieDto dto, MovieEntity movieEntity) {
        Set<MovieRewardEntity> movieRewardEntitySet = Sets.newHashSet();
        for (RewardDto rewardDto : dto.getRewardsDto().rewards()) {
            movieRewardEntitySet.add(
                    MovieRewardEntity.builder()
                            .movieRewardKey(
                                    MovieRewardKey.builder()
                                            .movieEntity(movieEntity)
                                            .rewardEntity(rewardRepository.findByName(rewardDto.name()).orElseThrow(() -> new CustomErrorException("category", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.BAD_REQUEST)))
                                            .build()
                            )
                            .awardReceivedDate(rewardDto.awardReceivedDate())
                            .build()
            );
        }
        movieEntity.setMovieRewardEntities(movieRewardEntitySet);
    }

    @Override
    public MovieDto toDto(MovieEntity entity) {
        var movieDto = movieMapper.toDto(entity);
        setCategoriesForMovieDto(entity, movieDto);
        return movieDto;
    }

    private void setCategoriesForMovieDto(MovieEntity entity, MovieDto movieDto) {
        Set<CategoryDto> categoriesSet = Sets.newHashSet();
        for (CategoryEntity categoryEntity : entity.getCategoryEntities()) {
            categoriesSet.add(categoryMapper.toDto(categoryEntity));
        }

        if (movieDto.getCategoriesDto() == null) {
            movieDto.setCategoriesDto(new CategoriesDto());
        }

        movieDto.getCategoriesDto().setCategories(categoriesSet);
    }
}
