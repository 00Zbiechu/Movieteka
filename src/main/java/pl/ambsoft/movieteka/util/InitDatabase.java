package pl.ambsoft.movieteka.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.model.entity.CategoryEntity;
import pl.ambsoft.movieteka.repository.CategoryRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDatabase {

    private final CategoryRepository categoryRepository;

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
            throw new CustomErrorException(e.getMessage(), ErrorCodes.FILE_ERROR, HttpStatus.BAD_REQUEST);
        }
    }
}
