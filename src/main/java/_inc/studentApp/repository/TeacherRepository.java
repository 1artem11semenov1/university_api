package _inc.studentApp.repository;

import _inc.studentApp.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    void deleteByEmail(String email);
    Teacher findByEmail(String email);
}