package pl.ambsoft.movieteka.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.ambsoft.movieteka.BaseTest;
import pl.ambsoft.movieteka.model.dto.wrapper.CategoriesDto;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class CategoryIT extends BaseTest {

    private final String PATH = "/api/category";

    @DisplayName("Should return all categories")
    @Test
    void shouldReturnAllCategories() throws Exception {

        //given:
        var expectedNumberOfCategories = 13;

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH));

        //then:
        var result = asObject(response, CategoriesDto.class);
        response.andExpect(status().isOk());
        Assertions.assertEquals(expectedNumberOfCategories, result.getCategories().size());
    }
}
