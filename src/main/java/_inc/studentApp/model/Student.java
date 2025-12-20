package _inc.studentApp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "students")
public class Student extends Person{
    @ManyToOne
    @JoinColumn(
            name = "group_id",
            foreignKey = @ForeignKey(
                    name = "group_id",
                    foreignKeyDefinition = "FOREIGN KEY (group_id) REFERENCES groups(id) ON UPDATE CASCADE ON DELETE SET NULL"
            )
    )
    private Group group;

    private String level;
    private String enterYear;
}
