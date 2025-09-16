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
    //private String group;
    private String level;
    private String enterYear;
}
