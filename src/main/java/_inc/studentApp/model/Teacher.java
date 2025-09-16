package _inc.studentApp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "teachers")
public class Teacher extends Person{
    private String position;
    private int experience;
}
