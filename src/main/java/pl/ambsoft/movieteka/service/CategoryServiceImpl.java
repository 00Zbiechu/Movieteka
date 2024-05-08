package pl.ambsoft.movieteka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.ambsoft.movieteka.mapper.CategoryMapper;
import pl.ambsoft.movieteka.model.dto.wrapper.CategoriesDto;
import pl.ambsoft.movieteka.repository.CategoryRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Cacheable(value = "categories")
    @Override
    public CategoriesDto getAllCategories() {
        return CategoriesDto.builder().categories(categoryRepository.findAll().stream().map(categoryMapper::toDto).collect(Collectors.toSet())).build();
    }
}
