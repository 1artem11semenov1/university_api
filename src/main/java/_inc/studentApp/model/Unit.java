package _inc.studentApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "units")
public class Unit {
    @Id
    @Column(name = "unit_name")
    String unitName;

    @NotBlank
    String address;

    @OneToMany(mappedBy = "unitName")
    List<ClassRoom> classrooms = new ArrayList<>();

    @OneToMany(mappedBy = "unitFrom")
    List<Distance> distances = new ArrayList<>();
}
