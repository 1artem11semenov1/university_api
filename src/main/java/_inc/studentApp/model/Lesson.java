package _inc.studentApp.model;

import _inc.studentApp.complexKeys.LessonKey;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "schedule")
public class Lesson {
    @EmbeddedId
    LessonKey id;

    @ManyToOne
    @MapsId("classroom")
    @JoinColumns({
            @JoinColumn(name = "classroom_number", referencedColumnName = "number"),
            @JoinColumn (name = "unit_name", referencedColumnName = "unit_name")
    })
    ClassRoom classroom;

    @ManyToOne
    @MapsId("discipline")
    @JoinColumns({
            @JoinColumn(name = "discipline_name", referencedColumnName = "discipline_name"),
            @JoinColumn (name = "group_name", referencedColumnName = "group_name"),
            @JoinColumn (name = "teacher_email", referencedColumnName = "teacher_email")
    })
    Disciplines discipline;
}
