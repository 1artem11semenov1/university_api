package _inc.studentApp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "employees")
public class Employee extends Person{
    @ManyToMany
    @JoinTable(
            name = "employees_positions",
            joinColumns = {@JoinColumn(name = "employee_id")},
            inverseJoinColumns = {@JoinColumn(name = "position_id")}
    )
    private Set<Position> positions = new HashSet<>();
    private int experience;

    @OneToMany(mappedBy = "teacherId")
    private List<Disciplines> disciplines = new ArrayList<>();
}
