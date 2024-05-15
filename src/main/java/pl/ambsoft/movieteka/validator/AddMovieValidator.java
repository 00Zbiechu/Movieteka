package pl.ambsoft.movieteka.validator;


import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.model.dto.AddMovieDto;
import pl.ambsoft.movieteka.model.dto.CategoryDto;
import pl.ambsoft.movieteka.repository.CategoryRepository;
import pl.ambsoft.movieteka.repository.MovieRepository;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AddMovieValidator implements Validator {

    protected final MovieRepository movieRepository;

    protected final CategoryRepository categoryRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AddMovieDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var dto = (AddMovieDto) target;
        validateIsMovieTitleUnique(dto);
        validateIsCategoryListIsNotNull(dto);
        validateIsCategoryListDoesNotContainDuplicate(dto);
        validateIsCategoriesExist(dto);
    }

    private void validateIsMovieTitleUnique(AddMovieDto dto) {
        movieRepository.findByTitle(dto.getTitle()).ifPresent(movie -> {
            throw new CustomErrorException(dto.getTitle(), ErrorCodes.ENTITY_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
        });
    }

    private void validateIsCategoryListIsNotNull(AddMovieDto dto) {
        if (dto.getCategories() == null) {
            throw new CustomErrorException("categories", ErrorCodes.FIELD_ERROR, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateIsCategoryListDoesNotContainDuplicate(AddMovieDto dto) {
        Set<CategoryDto> categoryDtoSet = Sets.newHashSet();
        for (CategoryDto categoryDto : dto.getCategories()) {
            if (!categoryDtoSet.add(categoryDto)) {
                throw new CustomErrorException("category", ErrorCodes.DUPLICATE_NAME, HttpStatus.BAD_REQUEST);
            }
        }
    }

    protected void validateIsCategoriesExist(AddMovieDto dto) {
        for (CategoryDto categoryDto : dto.getCategories()) {
            if (categoryDto.name() == null) {
                throw new CustomErrorException("category", ErrorCodes.FIELD_ERROR, HttpStatus.BAD_REQUEST);
            }
            categoryRepository.findByName(categoryDto.name()).orElseThrow(() -> new CustomErrorException(categoryDto.name(), ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
        }
    }
}
