package pl.ambsoft.movieteka.validatior;


import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.model.dto.CategoryDto;
import pl.ambsoft.movieteka.model.dto.MovieDto;
import pl.ambsoft.movieteka.model.entity.MovieEntity;
import pl.ambsoft.movieteka.repository.CategoryRepository;
import pl.ambsoft.movieteka.repository.MovieRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MovieValidator implements Validator {

    private final MovieRepository movieRepository;

    private final CategoryRepository categoryRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(MovieDto.class);
    }

    @Override
    public void validate(@NotNull Object target, @NotNull Errors errors) {
        var dto = (MovieDto) target;
        validateIsMovieTitleUnique(dto);
        validateIsCategoriesExist(dto);
    }

    private void validateIsMovieTitleUnique(MovieDto dto) {
        if (validateIsMovieTitleUniqueForUpdate(dto)) return;
        movieRepository.findByTitle(dto.getTitle()).ifPresent(movie -> {
            throw new CustomErrorException(dto.getTitle(), ErrorCodes.ENTITY_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
        });
    }

    private boolean validateIsMovieTitleUniqueForUpdate(MovieDto dto) {
        if (dto.getId() != null) {
            Optional<MovieEntity> movieEntity = movieRepository.findByTitle(dto.getTitle());
            if (movieEntity.isPresent() && !(movieEntity.get().getId().equals(dto.getId()))) {
                throw new CustomErrorException("title", ErrorCodes.ENTITY_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
            }
            return true;
        }
        return false;
    }

    private void validateIsCategoriesExist(MovieDto dto) {
        for (CategoryDto categoryDto : dto.getCategoriesDto().getCategories()) {
            categoryRepository.findByName(categoryDto.name()).orElseThrow(() -> {
                throw new CustomErrorException(categoryDto.name(), ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.BAD_REQUEST);
            });
        }
    }
}
