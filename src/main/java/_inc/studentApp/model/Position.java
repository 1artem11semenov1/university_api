package _inc.studentApp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "positions")
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String positionName;

    @ManyToMany(mappedBy = "positions")
    private Set<Employee> employees = new HashSet<>();
}
