package pl.ambsoft.movieteka.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import pl.ambsoft.movieteka.model.entity.key.MovieRewardKey;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class MovieRewardEntity {

    @EmbeddedId
    private MovieRewardKey movieRewardKey;

    @Column(nullable = false)
    private LocalDate awardReceivedDate;
}
