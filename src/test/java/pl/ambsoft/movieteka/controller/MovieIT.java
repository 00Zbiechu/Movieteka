package pl.ambsoft.movieteka.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.ambsoft.movieteka.BaseTest;
import pl.ambsoft.movieteka.model.dto.CategoryDto;
import pl.ambsoft.movieteka.model.dto.MovieDto;
import pl.ambsoft.movieteka.model.dto.wrapper.CategoriesDto;
import pl.ambsoft.movieteka.model.dto.wrapper.MoviesDto;
import pl.ambsoft.movieteka.model.entity.CategoryEntity;
import pl.ambsoft.movieteka.model.entity.MovieEntity;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MovieIT extends BaseTest {

    private final String PATH = "/api/movie";

    @DisplayName("Should get all movies")
    @Test
    void shouldGetAllMovies() throws Exception {

        //given
        entityManager.persist(MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .review(5.0f)
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        Set.of(CategoryEntity.builder()
                                .name("horror")
                                .build()
                        )
                )
                .build());

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH));

        //then
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movies.size()").value(1));
    }

    @DisplayName("Should add new movie")
    @Test
    void shouldAddNewMovie() throws Exception {

        //given
        var movie = MovieDto.builder()
                .title("Test")
                .description("Very nice movie")
                .review(5.0f)
                .yearOfProduction((short) 2004)
                .categoriesDto(
                        CategoriesDto.builder()
                                .categories(
                                        Set.of(CategoryDto.builder()
                                                .name("horror")
                                                .build()
                                        )
                                ).build()
                )
                .build();

        var movieJson = new MockMultipartFile(
                "movieDto",
                null,
                "application/json",
                asJson(movie).getBytes()
        );

        var expectedMovieList = List.of(
                MoviesDto.builder()
                        .movies(
                                List.of(movie))
                        .build()
        );

        byte[] imageBytes = Files.readAllBytes(Paths.get("src/main/resources/image/test_image.jpg"));

        MockMultipartFile photo = new MockMultipartFile("photo", "photo.png",
                "image/png", imageBytes);


        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.multipart(PATH)
                .file(movieJson)
                .file(photo)
        );

        //then
        response.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movies.size()").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movies[0].title").value(expectedMovieList.get(0).movies().get(0).getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movies[0].review").value(expectedMovieList.get(0).movies().get(0).getReview()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movies[0].description").value(expectedMovieList.get(0).movies().get(0).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movies[0].categoriesDto.categories.size()").value(expectedMovieList.get(0).movies().get(0).getCategoriesDto().getCategories().size()));
    }

    @DisplayName("Should return bad request cause wrong data in request body")
    @MethodSource("addMovieTestArguments")
    @ParameterizedTest
    void shouldReturnBadRequest(String title, String description, Float review, Short yearOfProduction, String category) throws Exception {

        var movie = MovieDto.builder()
                .title(title)
                .description(description)
                .review(review)
                .yearOfProduction(yearOfProduction)
                .categoriesDto(
                        CategoriesDto.builder()
                                .categories(
                                        Set.of(CategoryDto.builder()
                                                .name(category)
                                                .build()
                                        )
                                ).build()
                )
                .build();

        entityManager.persist(MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .review(5.0f)
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        Set.of(CategoryEntity.builder()
                                .name("horror")
                                .build()
                        )
                )
                .build());

        var movieJson = new MockMultipartFile(
                "movieDto",
                null,
                "application/json",
                asJson(movie).getBytes()
        );

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.multipart(PATH)
                .file(movieJson)
        );

        //then
        response.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> addMovieTestArguments() {
        return Stream.of(
                Arguments.of("Test", "Very nice movie", 4.4f, (short) 2004, "horror"),
                Arguments.of("", "Very nice movie", 4.4f, (short) 2004, "horror"),
                Arguments.of(null, "Very nice movie", 4.4f, (short) 2004, "horror"),
                Arguments.of("Title", "", 4.4f, (short) 2004, "horror"),
                Arguments.of("Title", null, 4.4f, (short) 2004, "horror"),
                Arguments.of("Title", "Very nice movie", null, (short) 2004, "horror"),
                Arguments.of("Title", "Very nice movie", 4.4f, null, "horror"),
                Arguments.of("Title", "Very nice movie", 4.4f, (short) 2004, null),
                Arguments.of("Title", "Very nice movie", 4.4f, (short) 2004, "melodrama")
        );
    }

    @DisplayName("Should delete movie")
    @Test
    void shouldDeleteMovie() throws Exception {

        //given
        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .review(5.0f)
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        Set.of(CategoryEntity.builder()
                                .name("horror")
                                .build()
                        )
                )
                .build();

        entityManager.persist(movieEntity);

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, PATH).param("id", movieEntity.getId().toString()));

        //then
        response.andExpect(status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movies.size()").value(0));
    }

    @DisplayName("Should delete movie cause entity does not exist")
    @Test
    void shouldNotDeleteMovieCauseEntityDoesNotExist() throws Exception {

        //given
        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .review(5.0f)
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        Set.of(CategoryEntity.builder()
                                .name("horror")
                                .build()
                        )
                )
                .build();

        entityManager.persist(movieEntity);

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, PATH).param("id", String.valueOf(movieEntity.getId() + 1)));

        //then
        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Should edit movie data")
    @Test
    void shouldEditMovieData() throws Exception {

        //given
        var categoryEntity = CategoryEntity.builder().name("horror").build();
        var categoryEntityTwo = CategoryEntity.builder().name("melodrama").build();

        entityManager.persist(categoryEntity);
        entityManager.persist(categoryEntityTwo);

        var movieEntity = MovieEntity.builder()
                .title("Test")
                .description("Very nice movie")
                .review(5.0f)
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        Set.of(categoryEntity)
                )
                .build();

        entityManager.persist(movieEntity);

        var movieDto = MovieDto.builder()
                .id(movieEntity.getId())
                .title("Test edit")
                .description("Very nice movie edit")
                .review(4.0f)
                .yearOfProduction((short) 2003)
                .categoriesDto(
                        CategoriesDto.builder()
                                .categories(
                                        Set.of(CategoryDto.builder()
                                                        .name("melodrama")
                                                        .build(),
                                                CategoryDto.builder()
                                                        .name("horror")
                                                        .build()
                                        )
                                ).build()
                )
                .build();

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.PUT, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(movieDto)));

        //then
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movies.size()").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movies[0].title").value(movieDto.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movies[0].review").value(movieDto.getReview()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movies[0].description").value(movieDto.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.movies[0].categoriesDto.categories.size()").value(2));
    }


    @DisplayName("Should filter and find 2 movies")
    @Test
    void shouldFilterAndFind2Movies() throws Exception {

        //given
        var categoryEntity = CategoryEntity.builder().name("horror").build();
        var categoryEntityTwo = CategoryEntity.builder().name("melodrama").build();

        entityManager.persist(categoryEntity);
        entityManager.persist(categoryEntityTwo);

        var movieEntityHorrorOne = MovieEntity.builder()
                .title("TestOne")
                .description("Very nice movie")
                .review(5.0f)
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        Set.of(categoryEntity)
                )
                .build();

        var movieEntityHorrorTwo = MovieEntity.builder()
                .title("TestTwo")
                .description("Very nice movie")
                .review(5.0f)
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        Set.of(categoryEntity)
                )
                .build();

        var movieEntityHorrorThree = MovieEntity.builder()
                .title("TestThree")
                .description("Very nice movie")
                .review(5.0f)
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        Set.of(categoryEntityTwo)
                )
                .build();

        entityManager.persist(movieEntityHorrorOne);
        entityManager.persist(movieEntityHorrorTwo);
        entityManager.persist(movieEntityHorrorThree);

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH + "/filter").param("category", "horror"));

        //then
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movies.size()").value(2));
    }

    @DisplayName("Should search and find movie by title")
    @Test
    void shouldSearchAndFindMovieByTitle() throws Exception {

        //given
        var categoryEntity = CategoryEntity.builder().name("horror").build();
        var categoryEntityTwo = CategoryEntity.builder().name("melodrama").build();

        entityManager.persist(categoryEntity);
        entityManager.persist(categoryEntityTwo);

        var movieEntityHorrorOne = MovieEntity.builder()
                .title("TestOne")
                .description("Very nice movie")
                .review(5.0f)
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        Set.of(categoryEntity)
                )
                .build();

        var movieEntityHorrorTwo = MovieEntity.builder()
                .title("TestTwo")
                .description("Very nice movie")
                .review(5.0f)
                .yearOfProduction((short) 2004)
                .categoryEntities(
                        Set.of(categoryEntity)
                )
                .build();

        entityManager.persist(movieEntityHorrorOne);
        entityManager.persist(movieEntityHorrorTwo);

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH + "/search").param("title", "One"));

        //then
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.movies.size()").value(1));
    }
}
