package _inc.studentApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "user_id")
    Long userId;

    @Column(name = "username", unique = true, nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    private String email;

    private String role;

    @OneToOne
    @JoinColumn(
            name = "student_id",
            foreignKey = @ForeignKey(
                    name = "users_students",
                    foreignKeyDefinition = "FOREIGN KEY (student_id) REFERENCES students(email) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    private Student student;

    @OneToOne
    @JoinColumn(
            name = "employee_id",
            foreignKey = @ForeignKey(
                    name = "users_employees",
                    foreignKeyDefinition = "FOREIGN KEY (employee_id) REFERENCES employees(email) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    private Employee employee;
}
