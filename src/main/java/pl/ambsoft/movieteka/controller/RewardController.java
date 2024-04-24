package pl.ambsoft.movieteka.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.ambsoft.movieteka.exception.wrapper.ErrorList;
import pl.ambsoft.movieteka.model.dto.wrapper.RewardsDto;
import pl.ambsoft.movieteka.service.RewardService;
import pl.ambsoft.movieteka.validator.RewardValidator;

@RestController
@RequestMapping("/api/reward")
@Tag(name = "Reward API")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    private final RewardValidator rewardValidator;

    @InitBinder("rewardsDto")
    public void validateReward(WebDataBinder binder) {
        binder.addValidators(rewardValidator);
    }

    @Operation(summary = "Get all rewards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all rewards",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RewardsDto.class))}),
            @ApiResponse(responseCode = "400", description = "Return error list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @GetMapping
    public ResponseEntity<RewardsDto> getRewards() {
        return new ResponseEntity<>(rewardService.getRewards(), HttpStatus.OK);
    }

    @Operation(summary = "Add new reward")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns all rewards",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RewardsDto.class))}),
            @ApiResponse(responseCode = "400", description = "Return error list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @PostMapping
    public ResponseEntity<RewardsDto> addNewRewards(@Valid @RequestBody RewardsDto rewardsDto) {
        return new ResponseEntity<>(rewardService.addNewRewards(rewardsDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete reward by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Returns all rewards",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RewardsDto.class))}),
            @ApiResponse(responseCode = "400", description = "Return error list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorList.class))})
    })
    @DeleteMapping
    public ResponseEntity<RewardsDto> deleteReward(@RequestParam @Parameter(description = "Reward ID", example = "1") Long id) {
        return new ResponseEntity<>(rewardService.deleteReward(id), HttpStatus.ACCEPTED);
    }
}
