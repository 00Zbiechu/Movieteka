package pl.ambsoft.movieteka.controller;

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
import pl.ambsoft.movieteka.model.dto.wrapper.MovieRewardsDto;
import pl.ambsoft.movieteka.model.entity.CategoryEntity;
import pl.ambsoft.movieteka.model.entity.MovieEntity;
import pl.ambsoft.movieteka.model.entity.MovieRewardEntity;
import pl.ambsoft.movieteka.model.entity.RewardEntity;
import pl.ambsoft.movieteka.model.entity.key.MovieRewardKey;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MovieRewardIT extends BaseTest {

    private final String PATH = "/api/movie-reward";

    @DisplayName("Should return list of all movies with rewards")
    @Test
    void shouldReturnListOfAllMoviesWithRewards() throws Exception {

        //given
        var categoryEntity = CategoryEntity.builder()
                .name("horror")
                .build();

        entityManager.persist(categoryEntity);

        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(categoryEntity)
                )
                .build();

        entityManager.persist(movieEntity);

        var rewardEntity = RewardEntity.builder()
                .name("Oscar")
                .build();

        var rewardEntityTwo = RewardEntity.builder()
                .name("Fryderyk")
                .build();

        entityManager.persist(rewardEntity);
        entityManager.persist(rewardEntityTwo);

        var movieRewardEntity = MovieRewardEntity.builder()
                .movieRewardKey(MovieRewardKey.builder()
                        .movieEntity(movieEntity)
                        .rewardEntity(rewardEntity)
                        .build())
                .awardReceivedDate(LocalDate.now())
                .build();

        var movieRewardEntityTwo = MovieRewardEntity.builder()
                .movieRewardKey(MovieRewardKey.builder()
                        .movieEntity(movieEntity)
                        .rewardEntity(rewardEntityTwo)
                        .build())
                .awardReceivedDate(LocalDate.now())
                .build();

        entityManager.persist(movieRewardEntity);
        entityManager.persist(movieRewardEntityTwo);

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH).param("movieId", movieEntity.getId().toString()));

        //then
        var result = asObject(response, MovieRewardsDto.class);
        response.andExpect(status().isOk());
        assertAll(
                () -> Assertions.assertEquals(2, result.movieRewards().size()),
                () -> Assertions.assertEquals(rewardEntity.getId(), result.movieRewards().get(0).id()),
                () -> Assertions.assertEquals(rewardEntity.getName(), result.movieRewards().get(0).name()),
                () -> Assertions.assertEquals(LocalDate.now(), result.movieRewards().get(0).awardReceivedDate()),
                () -> Assertions.assertEquals(rewardEntityTwo.getId(), result.movieRewards().get(1).id()),
                () -> Assertions.assertEquals(rewardEntityTwo.getName(), result.movieRewards().get(1).name()),
                () -> Assertions.assertEquals(LocalDate.now(), result.movieRewards().get(1).awardReceivedDate())
        );
    }

    @DisplayName("Should add reward to movie")
    @Test
    void shouldAddRewardToMovie() throws Exception {

        //given
        var categoryEntity = CategoryEntity.builder()
                .name("horror")
                .build();

        entityManager.persist(categoryEntity);

        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(categoryEntity)
                )
                .build();

        entityManager.persist(movieEntity);

        var rewardEntity = RewardEntity.builder()
                .name("Oscar")
                .build();

        entityManager.persist(rewardEntity);


        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .param("movieId", movieEntity.getId().toString())
                .param("rewardId", rewardEntity.getId().toString())
                .param("awardReceivedDate", LocalDate.now().toString())
        );

        //then
        var result = asObject(response, MovieRewardsDto.class);
        response.andExpect(status().isCreated());
        assertAll(
                () -> Assertions.assertEquals(1, result.movieRewards().size()),
                () -> Assertions.assertEquals(rewardEntity.getName(), result.movieRewards().get(0).name()),
                () -> Assertions.assertEquals(LocalDate.now(), result.movieRewards().get(0).awardReceivedDate())
        );
    }

    @DisplayName("Should not add reward to movie cause wrong request param data")
    @ParameterizedTest
    @MethodSource("addMovieRewardsTestArguments")
    void shouldNotAddRewardToMovieCauseWrongRequestDataParam(Long movieId, Long rewardId, LocalDate awardReceivedDate) throws Exception {

        //given
        var categoryEntity = CategoryEntity.builder()
                .name("horror")
                .build();

        entityManager.persist(categoryEntity);

        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(categoryEntity)
                )
                .build();

        entityManager.persist(movieEntity);

        var rewardEntity = RewardEntity.builder()
                .name("Oscar")
                .build();

        entityManager.persist(rewardEntity);

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .param("movieId", movieId.toString())
                .param("rewardId", rewardId.toString())
                .param("awardReceivedDate", awardReceivedDate.toString())
        );

        //then
        response.andExpect(status().isNotFound());
    }

    private static Stream<Arguments> addMovieRewardsTestArguments() {
        return Stream.of(
                Arguments.of(2L, 1L, LocalDate.now()),
                Arguments.of(1L, 2L, LocalDate.now())
        );
    }

    @DisplayName("Should remove reward from movie")
    @Test
    void shouldRemoveRewardFromMovie() throws Exception {

        //given
        var categoryEntity = CategoryEntity.builder()
                .name("horror")
                .build();

        entityManager.persist(categoryEntity);

        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(categoryEntity)
                )
                .build();

        entityManager.persist(movieEntity);

        var rewardEntity = RewardEntity.builder()
                .name("Oscar")
                .build();

        var rewardEntityTwo = RewardEntity.builder()
                .name("Fryderyk")
                .build();

        entityManager.persist(rewardEntity);
        entityManager.persist(rewardEntityTwo);

        var movieRewardEntity = MovieRewardEntity.builder()
                .movieRewardKey(MovieRewardKey.builder()
                        .movieEntity(movieEntity)
                        .rewardEntity(rewardEntity)
                        .build())
                .awardReceivedDate(LocalDate.now())
                .build();

        var movieRewardEntityTwo = MovieRewardEntity.builder()
                .movieRewardKey(MovieRewardKey.builder()
                        .movieEntity(movieEntity)
                        .rewardEntity(rewardEntityTwo)
                        .build())
                .awardReceivedDate(LocalDate.now())
                .build();

        entityManager.persist(movieRewardEntity);
        entityManager.persist(movieRewardEntityTwo);

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, PATH)
                .param("movieId", movieEntity.getId().toString())
                .param("rewardId", rewardEntity.getId().toString())
        );

        //then
        var result = asObject(response, MovieRewardsDto.class);
        response.andExpect(status().isOk());
        Assertions.assertEquals(1, result.movieRewards().size());
    }

    @DisplayName("Should not remove reward from movie and return bad request cause wrong data in request param")
    @Test
    void shouldNotRemoveRewardFromMovieCauseWrongDataInRequestParam() throws Exception {

        //given
        var categoryEntity = CategoryEntity.builder()
                .name("horror")
                .build();

        entityManager.persist(categoryEntity);

        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(categoryEntity)
                )
                .build();

        entityManager.persist(movieEntity);

        var rewardEntity = RewardEntity.builder()
                .name("Oscar")
                .build();

        var rewardEntityTwo = RewardEntity.builder()
                .name("Fryderyk")
                .build();

        entityManager.persist(rewardEntity);
        entityManager.persist(rewardEntityTwo);

        var movieRewardEntity = MovieRewardEntity.builder()
                .movieRewardKey(MovieRewardKey.builder()
                        .movieEntity(movieEntity)
                        .rewardEntity(rewardEntity)
                        .build())
                .awardReceivedDate(LocalDate.now())
                .build();

        var movieRewardEntityTwo = MovieRewardEntity.builder()
                .movieRewardKey(MovieRewardKey.builder()
                        .movieEntity(movieEntity)
                        .rewardEntity(rewardEntityTwo)
                        .build())
                .awardReceivedDate(LocalDate.now())
                .build();

        entityManager.persist(movieRewardEntity);
        entityManager.persist(movieRewardEntityTwo);

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, PATH)
                .param("movieId", String.valueOf(movieEntity.getId() + 1L))
                .param("rewardId", rewardEntity.getId().toString())
        );

        //then
        var result = asObject(response, ErrorList.class);
        response.andExpect(status().isBadRequest());
        assertAll(
                () -> Assertions.assertEquals(1, result.getErrorList().size()),
                () -> Assertions.assertEquals("movie", result.getErrorList().get(0).getField()),
                () -> Assertions.assertEquals(ErrorCodes.ENTITY_DOES_NOT_EXIST, result.getErrorList().get(0).getErrorCodes())
        );
    }
}
