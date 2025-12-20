package _inc.studentApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@Table(name = "classrooms")
public class ClassRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    Long id;

    @Column(name = "number")
    @NotBlank
    String classroomNumber;
    @Column(name = "unit_id")
    @NotNull
    Long unitID;

    int capacity;

    @ManyToOne
    @MapsId("unitID")
    @JoinColumn(
            name = "unit_id",
            foreignKey = @ForeignKey(
                    name = "units_classrooms",
                    foreignKeyDefinition = "FOREIGN KEY (unit_id) REFERENCES units(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    @JsonIgnore
    Unit unit;

    @JsonIgnore
    @OneToMany(mappedBy = "classroom")
    List<Lesson> lessons = new LinkedList<>();
}
