package pl.ambsoft.movieteka.validator;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.model.dto.MovieDto;
import pl.ambsoft.movieteka.model.entity.MovieEntity;
import pl.ambsoft.movieteka.repository.CategoryRepository;
import pl.ambsoft.movieteka.repository.MovieRepository;

import java.util.Optional;

@Component
public class EditMovieValidator extends AddMovieValidator implements Validator {

    public EditMovieValidator(MovieRepository movieRepository, CategoryRepository categoryRepository) {
        super(movieRepository, categoryRepository);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(MovieDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var dto = (MovieDto) target;
        validateIsMovieTitleUnique(dto);
        validateIsCategoriesExist(dto);
    }

    private void validateIsMovieTitleUnique(MovieDto dto) {
        if (dto.getId() != null) {
            Optional<MovieEntity> movieEntity = movieRepository.findByTitle(dto.getTitle());
            if (movieEntity.isPresent() && !(movieEntity.get().getId().equals(dto.getId()))) {
                throw new CustomErrorException("title", ErrorCodes.ENTITY_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
            }
        }
    }
}
