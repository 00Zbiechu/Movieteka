package pl.ambsoft.movieteka.model.entity;

import com.google.common.collect.Lists;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "REWARD")
public class RewardEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "movieRewardKey.rewardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MovieRewardEntity> movieRewardEntities = Lists.newArrayList();
}
