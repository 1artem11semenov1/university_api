package _inc.studentApp.model;

import _inc.studentApp.complexKeys.DisciplinesKey;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Disciplines {
    @EmbeddedId
    private DisciplinesKey id;

    @NotNull
    private int countHours;

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

    @OneToMany(mappedBy = "discipline")
    List<Lesson> lessons = new LinkedList<>();
}
