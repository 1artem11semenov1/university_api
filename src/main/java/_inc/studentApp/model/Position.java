package _inc.studentApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "positions")
@Getter
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    @NotBlank
    private String positionName;

    @ManyToMany(mappedBy = "positions")
    private Set<Employee> employees = new HashSet<>();
}
