package pl.ambsoft.movieteka.controller;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.ambsoft.movieteka.BaseTest;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.exception.wrapper.ErrorList;
import pl.ambsoft.movieteka.model.dto.RatingDto;
import pl.ambsoft.movieteka.model.dto.wrapper.RatingsDto;
import pl.ambsoft.movieteka.model.entity.CategoryEntity;
import pl.ambsoft.movieteka.model.entity.MovieEntity;
import pl.ambsoft.movieteka.model.entity.RatingEntity;

import java.util.List;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RatingIT extends BaseTest {

    private final String PATH = "/api/rating";

    @DisplayName("Should return list of all ratings")
    @Test
    void shouldReturnListOfAllRatings() throws Exception {

        //given:
        var categoryEntity = CategoryEntity.builder().name("horror").build();

        entityManager.persist(categoryEntity);

        var ratingEntity = RatingEntity.builder()
                .comment("Very nice movie")
                .score(4.4f)
                .build();

        entityManager.persist(ratingEntity);

        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(categoryEntity)
                )
                .ratingEntities(
                        List.of(ratingEntity)
                )
                .build();

        entityManager.persist(movieEntity);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH)
                .param("movieId", movieEntity.getId().toString()));

        //then:
        var result = asObject(response, RatingsDto.class);
        response.andExpect(status().isOk());
        assertAll(
                () -> Assertions.assertEquals(1, result.ratings().size()),
                () -> Assertions.assertEquals(ratingEntity.getComment(), result.ratings().get(0).comment()),
                () -> Assertions.assertEquals(ratingEntity.getScore(), result.ratings().get(0).score())
        );
    }

    @DisplayName("Should return bad request when add rating endpoint is hit cause wrong data")
    @ParameterizedTest
    @MethodSource("ratingTestArguments")
    void shouldReturnBadRequestWhenAddRatingEndpointIsHitCauseWrongData(String comment, Float score) throws Exception {

        //given:
        var categoryEntity = CategoryEntity.builder().name("horror").build();

        entityManager.persist(categoryEntity);

        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(categoryEntity)
                )
                .ratingEntities(Lists.newArrayList())
                .build();

        entityManager.persist(movieEntity);

        var ratingDto = RatingDto.builder()
                .comment(comment)
                .score(score)
                .build();

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(ratingDto))
                .param("movieId", movieEntity.getId().toString()));

        //then:
        response.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> ratingTestArguments() {
        return Stream.of(
                Arguments.of(null, 4.4f),
                Arguments.of(EMPTY, 4.4f),
                Arguments.of("Very nice movie", null),
                Arguments.of("Very nice movie", 5.5f),
                Arguments.of("Very nice movie", -5.5f)
        );
    }

    @DisplayName("Should return not found status when add rating endpoint is hit cause movie does not exist")
    @Test
    void shouldReturnNotFoundStatusWhenAddRatingEndpointIsHitCauseMovieDoesNotExist() throws Exception {

        //given:
        var categoryEntity = CategoryEntity.builder().name("horror").build();

        entityManager.persist(categoryEntity);

        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(categoryEntity)
                )
                .ratingEntities(Lists.newArrayList())
                .build();

        entityManager.persist(movieEntity);

        var ratingDto = RatingDto.builder()
                .comment("Very nice movie")
                .score(4.4f)
                .build();

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(ratingDto))
                .param("movieId", String.valueOf(movieEntity.getId() + 1)));

        //then:
        var result = asObject(response, ErrorList.class);
        response.andExpect(status().isNotFound());
        assertAll(
                () -> Assertions.assertEquals("movie", result.getErrorList().get(0).getField()),
                () -> Assertions.assertEquals(ErrorCodes.ENTITY_DOES_NOT_EXIST, result.getErrorList().get(0).getErrorCodes())
        );
    }
}
