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
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
