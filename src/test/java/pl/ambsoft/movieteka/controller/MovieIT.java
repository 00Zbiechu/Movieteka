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
import pl.ambsoft.movieteka.model.dto.AddMovieDto;
import pl.ambsoft.movieteka.model.dto.CategoryDto;
import pl.ambsoft.movieteka.model.dto.MovieDto;
import pl.ambsoft.movieteka.model.dto.wrapper.MoviesDto;
import pl.ambsoft.movieteka.model.entity.CategoryEntity;
import pl.ambsoft.movieteka.model.entity.MovieEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.querydsl.codegen.utils.Symbols.EMPTY;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MovieIT extends BaseTest {

    private final String PATH = "/api/movie";

    @DisplayName("Should return all movies")
    @Test
    void shouldReturnAllMovies() throws Exception {

        //given:
        Objects.requireNonNull(cacheManager.getCache("movies")).clear();

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
                .build();

        entityManager.persist(movieEntity);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH));

        //then:
        var result = asObject(response, MoviesDto.class);
        response.andExpect(status().isOk());
        assertAll(
                () -> Assertions.assertEquals(1, result.movies().size()),
                () -> Assertions.assertEquals(movieEntity.getId(), result.movies().get(0).getId()),
                () -> Assertions.assertEquals(movieEntity.getTitle(), result.movies().get(0).getTitle()),
                () -> Assertions.assertEquals(movieEntity.getDescription(), result.movies().get(0).getDescription()),
                () -> Assertions.assertEquals(movieEntity.getYearOfProduction(), result.movies().get(0).getYearOfProduction()),
                () -> Assertions.assertEquals(movieEntity.getCategoryEntities().size(), result.movies().get(0).getCategories().size()),
                () -> Assertions.assertEquals(movieEntity.getCategoryEntities().get(0).getName(), result.movies().get(0).getCategories().get(0).name())
        );
    }

    @DisplayName("Should add new movie")
    @Test
    void shouldAddNewMovie() throws Exception {

        //given:
        var movie = AddMovieDto.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categories(
                        List.of(CategoryDto.builder()
                                .name("horror")
                                .build()
                        )
                ).build();

        var expectedMovie = MovieDto.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categories(
                        List.of(CategoryDto.builder()
                                .name("horror")
                                .build()
                        )
                ).build();

        var expectedMovieList = List.of(
                MoviesDto.builder()
                        .movies(
                                List.of(expectedMovie))
                        .build()
        );


        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(movie))
        );

        //then:
        var result = asObject(response, MoviesDto.class);
        response.andExpect(status().isCreated());
        assertAll(
                () -> Assertions.assertEquals(1, result.movies().size()),
                () -> Assertions.assertEquals(expectedMovieList.get(0).movies().get(0).getTitle(), result.movies().get(0).getTitle()),
                () -> Assertions.assertEquals(expectedMovieList.get(0).movies().get(0).getDescription(), result.movies().get(0).getDescription()),
                () -> Assertions.assertEquals(expectedMovieList.get(0).movies().get(0).getTitle(), result.movies().get(0).getTitle()),
                () -> Assertions.assertEquals(expectedMovieList.get(0).movies().get(0).getCategories().size(), result.movies().get(0).getCategories().size())
        );
    }

    @DisplayName("Should return bad request when add movie endpoint is used cause wrong data in request body")
    @MethodSource("addMovieTestArguments")
    @ParameterizedTest
    void shouldReturnBadRequestWhenAddMovieEndpointIsUsed(String title, String description, Short yearOfProduction, String category) throws Exception {

        //given:
        var movie = AddMovieDto.builder()
                .title(title)
                .description(description)
                .yearOfProduction(yearOfProduction)
                .categories(
                        List.of(CategoryDto.builder()
                                .name(category)
                                .build()
                        )
                ).build();

        entityManager.persist(MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(CategoryEntity.builder()
                                .name("horror")
                                .build()
                        )
                )
                .build());

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PUT, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(movie))
        );

        //then:
        response.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> addMovieTestArguments() {
        return Stream.of(
                Arguments.of("Test", "Very nice movie", (short) 2004, "horror"),
                Arguments.of(EMPTY, "Very nice movie", (short) 2004, "horror"),
                Arguments.of(null, "Very nice movie", (short) 2004, "horror"),
                Arguments.of("Title", EMPTY, (short) 2004, "horror"),
                Arguments.of("Title", "Very nice movie", (short) 2004, "horror"),
                Arguments.of("Title", "Very nice movie", null, "horror"),
                Arguments.of("Title", "Very nice movie", (short) 2004, null),
                Arguments.of("Title", "Very nice movie", (short) 2004, "melodrama")
        );
    }

    @DisplayName("Should edit movie data")
    @Test
    void shouldEditMovieData() throws Exception {

        //given:
        var categoryEntity = CategoryEntity.builder().name("horror").build();
        var categoryEntityTwo = CategoryEntity.builder().name("melodrama").build();

        entityManager.persist(categoryEntityTwo);

        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(categoryEntity)
                )
                .build();

        entityManager.persist(movieEntity);

        var editMovieDto = MovieDto.builder()
                .id(movieEntity.getId())
                .title("Test edit")
                .description("Very nice movie edit")
                .yearOfProduction((short) 2003)
                .categories(
                        List.of(CategoryDto.builder()
                                        .name("melodrama")
                                        .build(),
                                CategoryDto.builder()
                                        .name("horror")
                                        .build()
                        )
                ).build();

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PUT, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(editMovieDto))
        );

        //then:
        var result = asObject(response, MoviesDto.class);
        response.andExpect(status().isOk());
        assertAll(
                () -> Assertions.assertEquals(1, result.movies().size()),
                () -> Assertions.assertEquals(editMovieDto.getTitle(), result.movies().get(0).getTitle()),
                () -> Assertions.assertEquals(editMovieDto.getDescription(), result.movies().get(0).getDescription()),
                () -> Assertions.assertEquals(editMovieDto.getTitle(), result.movies().get(0).getTitle()),
                () -> Assertions.assertEquals(editMovieDto.getCategories().size(), result.movies().get(0).getCategories().size())
        );
    }

    @DisplayName("Should delete movie")
    @Test
    void shouldDeleteMovie() throws Exception {

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
                .build();

        entityManager.persist(movieEntity);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, PATH).param("id", movieEntity.getId().toString()));

        //then:
        var result = asObject(response, MoviesDto.class);
        response.andExpect(status().isOk());
        Assertions.assertEquals(0, result.movies().size());
    }

    @DisplayName("Should not delete movie cause entity does not exist")
    @Test
    void shouldNotDeleteMovieCauseEntityDoesNotExist() throws Exception {

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
                .build();

        entityManager.persist(movieEntity);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, PATH).param("id", String.valueOf(movieEntity.getId() + 1)));

        //then:
        response.andExpect(status().isNotFound());
    }


    @DisplayName("Should filter movieRewards by category and find movieRewards")
    @ParameterizedTest
    @MethodSource("filterTestArguments")
    void shouldFilterMoviesAndByCategoryAndFindMovies(String category, int size) throws Exception {

        //given:
        var categoryEntity = CategoryEntity.builder().name("horror").build();
        var categoryEntityTwo = CategoryEntity.builder().name("melodrama").build();
        var categoryEntityThree = CategoryEntity.builder().name("sci-fi").build();

        entityManager.persist(categoryEntity);
        entityManager.persist(categoryEntityTwo);
        entityManager.persist(categoryEntityThree);

        var movieEntityHorrorOne = MovieEntity.builder()
                .title("TestOne")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(categoryEntity)
                )
                .build();

        var movieEntityHorrorTwo = MovieEntity.builder()
                .title("TestTwo")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(categoryEntity)
                )
                .build();

        var movieEntityMelodramaThree = MovieEntity.builder()
                .title("TestThree")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(categoryEntityTwo)
                )
                .build();

        entityManager.persist(movieEntityHorrorOne);
        entityManager.persist(movieEntityHorrorTwo);
        entityManager.persist(movieEntityMelodramaThree);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH + "/filter").param("category", category));

        //then:
        var result = asObject(response, MoviesDto.class);
        response.andExpect(status().isOk());
        Assertions.assertEquals(size, result.movies().size());
    }

    private static Stream<Arguments> filterTestArguments() {
        return Stream.of(
                Arguments.of("horror", 2),
                Arguments.of("melodrama", 1),
                Arguments.of("sci-fi", 0)
        );
    }

    @DisplayName("Should search and find movie by title")
    @ParameterizedTest
    @MethodSource("searchTestArguments")
    void shouldSearchAndFindMovieByTitle(String title, int size) throws Exception {

        //given:
        var categoryEntity = CategoryEntity.builder().name("horror").build();
        var categoryEntityTwo = CategoryEntity.builder().name("melodrama").build();

        entityManager.persist(categoryEntity);
        entityManager.persist(categoryEntityTwo);

        var movieEntityHorrorOne = MovieEntity.builder()
                .title("TestOne")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(categoryEntity)
                )
                .build();

        var movieEntityHorrorTwo = MovieEntity.builder()
                .title("TestTwo")
                .description("Very nice movie")
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        List.of(categoryEntity)
                )
                .build();

        entityManager.persist(movieEntityHorrorOne);
        entityManager.persist(movieEntityHorrorTwo);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH + "/search").param("title", title));

        //then:
        var result = asObject(response, MoviesDto.class);
        response.andExpect(status().isOk());
        Assertions.assertEquals(size, result.movies().size());
    }

    private static Stream<Arguments> searchTestArguments() {
        return Stream.of(
                Arguments.of("One", 1),
                Arguments.of("Two", 1),
                Arguments.of("Test", 2),
                Arguments.of("Title", 0)
        );
    }
}
