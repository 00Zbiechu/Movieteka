package pl.ambsoft.movieteka.controller;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.ambsoft.movieteka.BaseTest;
import pl.ambsoft.movieteka.model.dto.wrapper.CoversDto;
import pl.ambsoft.movieteka.model.entity.CategoryEntity;
import pl.ambsoft.movieteka.model.entity.CoverEntity;
import pl.ambsoft.movieteka.model.entity.MovieEntity;
import pl.ambsoft.movieteka.type.ImageType;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CoverIT extends BaseTest {

    private final String PATH = "/api/cover";

    @DisplayName("Should return cover for movie")
    @Test
    void shouldReturnCoverForMovie() throws Exception {

        //given:
        byte[] photo = {1, 2, 3};

        var coverEntity = CoverEntity.builder()
                .photo(photo)
                .format(ImageType.JPEG)
                .name("Test")
                .build();

        entityManager.persist(coverEntity);

        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(CategoryEntity.builder()
                                .name("horror")
                                .build()
                        )
                )
                .coverEntities(List.of(coverEntity))
                .build();

        entityManager.persist(movieEntity);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH).param("movieId", movieEntity.getId().toString()));

        //then:
        var result = asObject(response, CoversDto.class);
        response.andExpect(status().isOk());
        assertAll(
                () -> Assertions.assertEquals(1, result.covers().size()),
                () -> Assertions.assertEquals(coverEntity.getName(), result.covers().get(0).name()),
                () -> Assertions.assertEquals(coverEntity.getFormat().name(), result.covers().get(0).format())
        );
    }

    @DisplayName("Should return not found cause movie does not exist")
    @Test
    void shouldReturnBadRequestCauseMovieDoesNotExist() throws Exception {

        //given:
        Objects.requireNonNull(cacheManager.getCache("coverByMovieId")).clear();

        byte[] photo = {1, 2, 3};

        var coverEntity = CoverEntity.builder()
                .photo(photo)
                .format(ImageType.JPEG)
                .name("Test")
                .build();

        entityManager.persist(coverEntity);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH).param("movieId", "1"));

        //then:
        response.andExpect(status().isNotFound());
    }

    @DisplayName("Should return empty list cause cover was not set")
    @Test
    void shouldReturnEmptyListCauseCoverWasNotSet() throws Exception {

        //given:
        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(CategoryEntity.builder()
                                .name("horror")
                                .build()
                        )
                )
                .coverEntities(Lists.newArrayList())
                .build();

        entityManager.persist(movieEntity);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH).param("movieId", movieEntity.getId().toString()));

        //then:
        var result = asObject(response, CoversDto.class);
        response.andExpect(status().isOk());
        assertAll(
                () -> Assertions.assertEquals(0, result.covers().size())
        );
    }

    /*
    @DisplayName("Should add new cover for movie")
    @Test
    void shouldAddNewCoverForMovie() throws Exception {

        //given:
        Objects.requireNonNull(cacheManager.getCache("coverByMovieId")).clear();

        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(Lists.newArrayList())
                .coverEntities(new ArrayList<>())
                .build();

        entityManager.persist(movieEntity);

        byte[] imageBytes = Files.readAllBytes(Paths.get("src/main/resources/image/test_image.jpg"));

        MockMultipartFile photo = new MockMultipartFile("cover", "photo.png",
                "image/png", imageBytes);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.multipart(PATH)
                .file(photo)
                .param("movieId", movieEntity.getId().toString())
        );

        //then:
        var result = asObject(response, CoversDto.class);
        response.andExpect(status().isCreated());
        assertAll(
                () -> Assertions.assertEquals(1, result.covers().size()),
                () -> Assertions.assertEquals(photo.getName(), result.covers().get(0).name())
        );
    }
     */

    @DisplayName("Should return empty list of covers after delete")
    @Test
    void shouldReturnEmptyListOfCoversAfterDelete() throws Exception {

        //given:
        Objects.requireNonNull(cacheManager.getCache("coverByMovieId")).clear();

        byte[] photo = {1, 2, 3};

        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(Lists.newArrayList())
                .coverEntities(Lists.newArrayList())
                .build();

        var coverEntity = CoverEntity.builder()
                .photo(photo)
                .format(ImageType.JPEG)
                .name("Test")
                .movieEntity(movieEntity)
                .build();

        entityManager.persist(coverEntity);

        entityManager.persist(movieEntity);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, PATH).param("movieId", movieEntity.getId().toString()));

        //then:
        var result = asObject(response, CoversDto.class);
        response.andExpect(status().isOk());
        assertAll(
                () -> Assertions.assertEquals(0, result.covers().size())
        );
    }

    @DisplayName("Should return not found when delete endpoint is hit cause movie does not exist")
    @Test
    void shouldReturnNotFoundWhenDeleteEndpointIsHitCauseMovieDoesNotExist() throws Exception {

        //given:
        Objects.requireNonNull(cacheManager.getCache("coverByMovieId")).clear();

        byte[] photo = {1, 2, 3};

        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(Lists.newArrayList())
                .coverEntities(Lists.newArrayList())
                .build();

        var coverEntity = CoverEntity.builder()
                .photo(photo)
                .format(ImageType.JPEG)
                .name("Test")
                .movieEntity(movieEntity)
                .build();

        entityManager.persist(coverEntity);

        entityManager.persist(movieEntity);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, PATH).param("movieId", String.valueOf(movieEntity.getId() + 1)));

        //then:
        var result = asObject(response, CoversDto.class);
        response.andExpect(status().isNotFound());
    }
}
