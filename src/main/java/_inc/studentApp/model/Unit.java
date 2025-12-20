package _inc.studentApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "units")
@NoArgsConstructor
@Setter
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;

    @Column(name = "unit_name", unique = true)
    @NotBlank
    String unitName;

    @NotBlank
    String address;

    @JsonIgnore
    @OneToMany(mappedBy = "unit")
    List<ClassRoom> classrooms = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "unitF")
    List<Distance> distances = new ArrayList<>();
}
