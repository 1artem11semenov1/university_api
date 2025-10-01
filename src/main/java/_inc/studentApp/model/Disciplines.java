package _inc.studentApp.model;

import _inc.studentApp.complexKeys.DisciplinesKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Disciplines {
    @EmbeddedId
    private DisciplinesKey id;

    @ManyToOne
    @MapsId("groupName")
    @JoinColumn(name = "group_name")
    private Group groupName;

    @ManyToOne
    @MapsId("teacherEmail")
    @JoinColumn(name = "teacher_email")
    private Employee teacherEmail;

    public String getDisciplineName(){
        return this.id.getName();
    }
}
