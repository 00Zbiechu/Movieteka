package pl.ambsoft.movieteka.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.ambsoft.movieteka.BaseTest;
import pl.ambsoft.movieteka.model.dto.RewardDto;
import pl.ambsoft.movieteka.model.dto.wrapper.RewardsDto;
import pl.ambsoft.movieteka.model.entity.RewardEntity;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RewardIT extends BaseTest {

    private final String PATH = "/api/reward";

    @DisplayName("Should return list of rewards")
    @Test
    void shouldReturnListOfRewards() throws Exception {

        //given
        var rewardEntity = RewardEntity.builder()
                .name("Oscar")
                .build();

        entityManager.persist(rewardEntity);

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, PATH));

        //then
        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.rewards.size()").value(1));
    }

    @DisplayName("Should add new reward")
    @Test
    void shouldAddNewReward() throws Exception {

        //given
        RewardsDto rewardsDto = RewardsDto.builder()
                .rewards(
                        Set.of(
                                RewardDto.builder()
                                        .name("Oscar")
                                        .build()
                        )
                )
                .build();

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(rewardsDto)));

        //then
        response.andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.rewards.size()").value(1));
    }

    @DisplayName("Should return bad request cause blank name")
    @Test
    void shouldReturnBadRequestCauseBlankName() throws Exception {

        //given
        RewardsDto rewardsDto = RewardsDto.builder()
                .rewards(
                        Set.of(
                                RewardDto.builder()
                                        .name("")
                                        .build()
                        )
                )
                .build();

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(rewardsDto)));

        //then
        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Should delete reward")
    @Test
    void shouldDeleteReward() throws Exception {

        //given
        var rewardEntity = RewardEntity.builder()
                .name("Oscar")
                .build();

        entityManager.persist(rewardEntity);

        //when
        var response = mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.DELETE, PATH)
                .param("id", rewardEntity.getId().toString()));

        //then
        response.andExpect(status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.rewards.size()").value(0));
    }
}
