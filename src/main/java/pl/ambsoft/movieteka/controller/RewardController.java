package pl.ambsoft.movieteka.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.ambsoft.movieteka.model.dto.wrapper.RewardsDto;
import pl.ambsoft.movieteka.service.RewardService;

@RestController
@RequestMapping("/api/reward")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    @GetMapping
    public ResponseEntity<RewardsDto> getRewards() {
        return new ResponseEntity<>(rewardService.getRewards(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RewardsDto> addNewRewards(@Valid @RequestBody RewardsDto rewardsDto) {
        return new ResponseEntity<>(rewardService.addNewRewards(rewardsDto), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<RewardsDto> deleteReward(@RequestParam Long id) {
        return new ResponseEntity<>(rewardService.deleteReward(id), HttpStatus.ACCEPTED);
    }
}
