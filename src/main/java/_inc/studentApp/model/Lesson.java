package _inc.studentApp.model;

import _inc.studentApp.complexKeys.LessonKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "schedule")
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    @EmbeddedId
    LessonKey id;

    @ManyToOne
    @MapsId("classroom")
    @JoinColumn(
            name = "classroom_id",
            foreignKey = @ForeignKey(
                    name = "schedule_classrooms",
                    foreignKeyDefinition = "FOREIGN KEY (classroom_id) REFERENCES classrooms(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    /*@JoinColumns(
            foreignKey = @ForeignKey(
                    name = "classrooms_schedule",
                    foreignKeyDefinition = "FOREIGN KEY (classroom_number, unit_name) REFERENCES classrooms(number, unit_name) ON UPDATE CASCADE ON DELETE CASCADE"
            ),
            value = {
                    @JoinColumn(
                            name = "classroom_number",
                            referencedColumnName = "number"
                    ),
                    @JoinColumn(
                            name = "unit_name",
                            referencedColumnName = "unit_name"
                    )
            }
    )*/
    ClassRoom classroom;

    @ManyToOne
    @MapsId("discipline")
    @JoinColumn(
            name = "discipline_id",
            foreignKey = @ForeignKey(
                    name = "schedule_disciplines",
                    foreignKeyDefinition = "FOREIGN KEY (discipline_id) REFERENCES disciplines(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    /*@JoinColumns(
            foreignKey = @ForeignKey(
                    name = "disciplines_schedule",
                    foreignKeyDefinition = "FOREIGN KEY (teacher_email, discipline_name, group_name) REFERENCES disciplines(teacher_email, discipline_name, group_name) ON UPDATE CASCADE ON DELETE CASCADE"
            ),
            value = {
                    @JoinColumn(
                            name = "discipline_name",
                            referencedColumnName = "discipline_name"
                    ),
                    @JoinColumn(
                            name = "group_name",
                            referencedColumnName = "group_name"
                    ),
                    @JoinColumn(
                            name = "teacher_email",
                            referencedColumnName = "teacher_email"
                    )
            }
    )*/
    Disciplines discipline;
}
