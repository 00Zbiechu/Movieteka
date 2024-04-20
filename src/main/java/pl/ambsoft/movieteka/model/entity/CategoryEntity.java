package pl.ambsoft.movieteka.model.entity;

import com.google.common.collect.Lists;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
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
@Table(name = "CATEGORY")
public class CategoryEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "categoryEntities")
    private List<MovieEntity> movieEntities = Lists.newArrayList();
}
