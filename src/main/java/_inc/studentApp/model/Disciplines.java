package _inc.studentApp.model;

import _inc.studentApp.complexKeys.DisciplinesKey;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Disciplines {
    @EmbeddedId
    private DisciplinesKey id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Employee teacherId;

    @ManyToOne
    @MapsId("groupName")
    @JoinColumn(name = "group_name")
    private Group groupName;
}
