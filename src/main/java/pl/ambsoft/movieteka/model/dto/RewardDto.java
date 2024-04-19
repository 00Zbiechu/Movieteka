package pl.ambsoft.movieteka.model.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record RewardDto(Long id, String name, LocalDate awardReceivedDate) {
}
