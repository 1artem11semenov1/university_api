package _inc.studentApp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "students")
//@DiscriminatorValue("STUDENT")
public class Student extends Person{
    @ManyToOne
    @JoinColumn(
            name = "group_id",
            foreignKey = @ForeignKey(
                    name = "group_id",
                    foreignKeyDefinition = "FOREIGN KEY (group_id) REFERENCES groups(group_name) ON UPDATE CASCADE ON DELETE SET NULL"
            )
    )
    private Group group;
    private String level;
    private String enterYear;
}
