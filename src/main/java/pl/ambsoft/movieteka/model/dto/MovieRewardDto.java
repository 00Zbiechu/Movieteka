package pl.ambsoft.movieteka.model.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MovieRewardDto(Long id, String name, LocalDate awardReceivedDate) {
}
