package pl.ambsoft.movieteka.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.ambsoft.movieteka.BaseTest;
import pl.ambsoft.movieteka.model.dto.CategoryDto;
import pl.ambsoft.movieteka.model.dto.MovieDto;
import pl.ambsoft.movieteka.model.dto.wrapper.CategoriesDto;
import pl.ambsoft.movieteka.model.dto.wrapper.MoviesDto;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MovieIT extends BaseTest {

    private final String PATH = "/api/movie";

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
}
