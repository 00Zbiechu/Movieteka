package pl.ambsoft.movieteka.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.ambsoft.movieteka.model.entity.CategoryEntity;
import pl.ambsoft.movieteka.repository.CategoryRepository;

@Component
@RequiredArgsConstructor
public class InitDatabase {

    private final CategoryRepository categoryRepository;

    //TODO: Not working in docker-compose
    /*
    @Value("${csv.category.init.path}")
    private String categoryInitPath;


    @PostConstruct
    private void initDatabaseWithMovieCategory() {
        if (!categoryRepository.findAll().isEmpty()) {
            return;
        }

        try (CSVParser csvParser = new CSVParser(new FileReader(categoryInitPath), CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            List<CSVRecord> list = csvParser.getRecords();
            for (CSVRecord row : list) {
                for (String entry : row) {
                    categoryRepository.save(
                            CategoryEntity.builder()
                                    .name(entry)
                                    .build()
                    );
                }
            }
        } catch (IOException e) {
            throw new CustomErrorException(e.getMessage(), ErrorCodes.FILE_ERROR, HttpStatus.NO_CONTENT);
        }
    }
    */

    @PostConstruct
    private void initDatabaseWithMovieCategory() {
        categoryRepository.save(CategoryEntity.builder().name("action").build());
        categoryRepository.save(CategoryEntity.builder().name("anime").build());
        categoryRepository.save(CategoryEntity.builder().name("drama").build());
        categoryRepository.save(CategoryEntity.builder().name("fantasy").build());
        categoryRepository.save(CategoryEntity.builder().name("horror").build());
        categoryRepository.save(CategoryEntity.builder().name("criminal").build());
        categoryRepository.save(CategoryEntity.builder().name("sci-fi").build());
        categoryRepository.save(CategoryEntity.builder().name("sport").build());
        categoryRepository.save(CategoryEntity.builder().name("thriller").build());
        categoryRepository.save(CategoryEntity.builder().name("comedy").build());
        categoryRepository.save(CategoryEntity.builder().name("musical").build());
        categoryRepository.save(CategoryEntity.builder().name("adventure").build());
        categoryRepository.save(CategoryEntity.builder().name("western").build());
    }
}
