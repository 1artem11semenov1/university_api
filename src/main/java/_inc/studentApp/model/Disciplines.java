package _inc.studentApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
public class Disciplines {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @NotBlank
    private String disciplineName;
    @NotNull
    private Long groupID;
    @NotNull
    private Long teacherID;

    @NotNull
    private int countHours;

    @ManyToOne
    @MapsId("groupID")
    @JoinColumn(
            name = "group_id",
            foreignKey = @ForeignKey(
                    name = "disciplines_groups",
                    foreignKeyDefinition = "FOREIGN KEY (group_id) REFERENCES groups(id) ON UPDATE CASCADE ON DELETE RESTRICT"
            ))
    private Group group;

    @ManyToOne
    @MapsId("teacherID")
    @JoinColumn(
            name = "teacher_id",
            foreignKey = @ForeignKey(
                    name = "disciplines_employees",
                    foreignKeyDefinition = "FOREIGN KEY (teacher_id) REFERENCES employees(id) ON UPDATE CASCADE ON DELETE RESTRICT"
            )
    )
    private Employee teacher;

    @OneToMany(mappedBy = "discipline")
    List<Lesson> lessons = new LinkedList<>();

    public void setWithoutId(String disciplineName, Long groupID, Long teacherID, int countHours, Group group, Employee teacher, List<Lesson> lessons){
        this.disciplineName = disciplineName;
        this.groupID = groupID;
        this.teacherID = teacherID;
        this.countHours = countHours;
        this.group = group;
        this.teacher = teacher;
        this.lessons = lessons;
    }
}
