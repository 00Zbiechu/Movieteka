package pl.ambsoft.movieteka.model.entity;

import com.google.common.collect.Lists;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MOVIE")
public class MovieEntity extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Short yearOfProduction;

    @Column(nullable = false)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "movie_category",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<CategoryEntity> categoryEntities = Lists.newArrayList();

    @OneToMany(mappedBy = "movieRewardKey.movieEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MovieRewardEntity> movieRewardEntities = Lists.newArrayList();

    @OneToMany(mappedBy = "movieEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<RatingEntity> ratingEntities = Lists.newArrayList();

    @OneToMany(mappedBy = "movieEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CoverEntity> coverEntities = Lists.newArrayList();
}
