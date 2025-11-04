package _inc.studentApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLUpdate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "employees")
@NoArgsConstructor
@Setter
public class Employee extends Person{
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "employees_positions",
            joinColumns = {@JoinColumn(name = "employee_id")},
            inverseJoinColumns = {@JoinColumn(name = "position_id")}
    )
    @JsonIgnore
    private List<Position> positions = new ArrayList<>();
    private int experience;

    @OneToMany(mappedBy = "teacherEmail")
    @JsonIgnore
    private List<Disciplines> disciplines = new ArrayList<>();

}
