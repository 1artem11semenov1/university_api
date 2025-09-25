package _inc.studentApp.repository;

import _inc.studentApp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    void deleteByEmail(String email);
    Employee findByEmail(String email);
}