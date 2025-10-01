package _inc.studentApp.repository;

import _inc.studentApp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    void deleteByEmail(String email);
    Optional<Employee> findByEmail(String email);
}