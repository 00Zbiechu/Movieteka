package pl.ambsoft.movieteka.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.ambsoft.movieteka.BaseTest;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.exception.wrapper.ErrorList;
import pl.ambsoft.movieteka.model.dto.RewardDto;
import pl.ambsoft.movieteka.model.dto.wrapper.RewardsDto;
import pl.ambsoft.movieteka.model.entity.RewardEntity;

import java.util.List;

import static com.querydsl.codegen.utils.Symbols.EMPTY;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RewardIT extends BaseTest {

    private final String PATH = "/api/reward";

    @DisplayName("Should return list of rewards")
    @Test
    void shouldReturnListOfRewards() throws Exception {

        //given:
        var rewardEntity = RewardEntity.builder()
                .name("Oscar")
                .build();

        entityManager.persist(rewardEntity);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH));

        //then:
        var result = asObject(response, RewardsDto.class);
        response.andExpect(status().isOk());
        assertAll(
                () -> Assertions.assertEquals(1, result.rewards().size()),
                () -> Assertions.assertEquals(rewardEntity.getId(), result.rewards().get(0).id()),
                () -> Assertions.assertEquals("Oscar", result.rewards().get(0).name())
        );
    }

    @DisplayName("Should add new reward")
    @Test
    void shouldAddNewReward() throws Exception {

        //given:
        RewardsDto rewardsDto = RewardsDto.builder()
                .rewards(
                        List.of(
                                RewardDto.builder()
                                        .name("Oscar")
                                        .build()
                        )
                )
                .build();

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(rewardsDto)));

        //then:
        var result = asObject(response, RewardsDto.class);
        response.andExpect(status().isCreated());
        assertAll(
                () -> Assertions.assertEquals(1, result.rewards().size()),
                () -> Assertions.assertEquals("Oscar", result.rewards().get(0).name())
        );
    }

    @DisplayName("Should return bad request when add method cause blank name of reward name")
    @Test
    void shouldReturnBadRequestWhenAddMethodCauseBlankNameOfRewardName() throws Exception {

        //given:
        RewardsDto rewardsDto = RewardsDto.builder()
                .rewards(
                        List.of(
                                RewardDto.builder()
                                        .name(EMPTY)
                                        .build()
                        )
                )
                .build();

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(rewardsDto)));

        //then:
        var result = asObject(response, ErrorList.class);
        response.andExpect(status().isBadRequest());
        assertAll(
                () -> assertEquals(1, result.getErrorList().size()),
                () -> assertEquals("rewards[0].name", result.getErrorList().get(0).getField()),
                () -> assertEquals(ErrorCodes.FIELD_ERROR, result.getErrorList().get(0).getErrorCodes())
        );
    }

    @DisplayName("Should return bad request when add method cause empty reward list")
    @Test
    void shouldReturnBadRequestWhenAddMethodCauseEmptyRewardList() throws Exception {

        //given:
        RewardsDto rewardsDto = RewardsDto.builder()
                .rewards(
                        List.of(
                                RewardDto.builder()
                                        .name("Oscar")
                                        .build(),
                                RewardDto.builder()
                                        .name("Oscar")
                                        .build()
                        )
                )
                .build();

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(rewardsDto)));

        //then:
        var result = asObject(response, ErrorList.class);
        response.andExpect(status().isBadRequest());
        assertAll(
                () -> assertEquals(1, result.getErrorList().size()),
                () -> assertEquals("Oscar", result.getErrorList().get(0).getField()),
                () -> assertEquals(ErrorCodes.DUPLICATE_NAME, result.getErrorList().get(0).getErrorCodes())
        );
    }

    @DisplayName("Should return bad request when add method cause duplicate reward name in list")
    @Test
    void shouldReturnBadRequestWhenAddMethodCauseDuplicateRewardNameInList() throws Exception {

        //given:
        RewardsDto rewardsDto = RewardsDto.builder()
                .rewards(
                        List.of()
                )
                .build();

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(rewardsDto)));

        //then:
        var result = asObject(response, ErrorList.class);
        response.andExpect(status().isBadRequest());
        assertAll(
                () -> assertEquals(1, result.getErrorList().size()),
                () -> assertEquals("rewards", result.getErrorList().get(0).getField()),
                () -> assertEquals(ErrorCodes.FIELD_ERROR, result.getErrorList().get(0).getErrorCodes())
        );
    }

    @DisplayName("Should delete reward")
    @Test
    void shouldDeleteReward() throws Exception {

        //given:
        var rewardEntity = RewardEntity.builder()
                .name("Oscar")
                .build();

        entityManager.persist(rewardEntity);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, PATH)
                .param("id", rewardEntity.getId().toString()));

        //then:
        var result = asObject(response, RewardsDto.class);
        response.andExpect(status().isOk());
        Assertions.assertEquals(0, result.rewards().size());
    }

    @DisplayName("Should return bad request when delete reward endpoint is hit cause reward with id does not exist")
    @Test
    void shouldReturnBadRequestWhenDeleteRewardEndpointIsHitCauseRewardWithIdDoesNotExist() throws Exception {

        //given:
        var rewardEntity = RewardEntity.builder()
                .name("Oscar")
                .build();

        entityManager.persist(rewardEntity);

        //when:
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, PATH)
                .param("id", String.valueOf(rewardEntity.getId() + 1)));

        //then:
        response.andExpect(status().isNotFound());
    }
}
