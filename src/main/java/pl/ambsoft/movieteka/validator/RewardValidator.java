package pl.ambsoft.movieteka.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.model.dto.RewardDto;
import pl.ambsoft.movieteka.model.dto.wrapper.RewardsDto;
import pl.ambsoft.movieteka.repository.RewardRepository;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RewardValidator implements Validator {

    private final RewardRepository rewardRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RewardsDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        var dto = (RewardsDto) target;
        validateIsNameUnique(dto);
    }

    private void validateIsNameUnique(RewardsDto rewardsDto) {
        Set<String> uniqueNames = new HashSet<>();
        for (RewardDto rewardDto : rewardsDto.rewards()) {
            if (rewardDto.name() == null) {
                throw new CustomErrorException("reward", ErrorCodes.FIELD_ERROR, HttpStatus.BAD_REQUEST);
            }
            if (!uniqueNames.add(rewardDto.name())) {
                throw new CustomErrorException(rewardDto.name(), ErrorCodes.DUPLICATE_NAME, HttpStatus.BAD_REQUEST);
            }
            rewardRepository.findByName(rewardDto.name()).ifPresent(reward -> {
                throw new CustomErrorException(reward.getName(), ErrorCodes.ENTITY_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
            });
        }
    }
}