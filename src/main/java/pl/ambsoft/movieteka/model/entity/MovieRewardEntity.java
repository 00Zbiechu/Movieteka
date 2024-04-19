package pl.ambsoft.movieteka.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.ambsoft.movieteka.model.entity.key.MovieRewardKey;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MOVIE_REWARD")
public class MovieRewardEntity {

    @EmbeddedId
    private MovieRewardKey movieRewardKey;

    @Column(nullable = false)
    private LocalDate awardReceivedDate;
}
