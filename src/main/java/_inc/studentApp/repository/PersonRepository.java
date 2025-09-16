package _inc.studentApp.repository;

import _inc.studentApp.model.Person;
import _inc.studentApp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface PersonRepository extends JpaRepository<Person, Long> {
    void deleteByEmail(String email);
    Person findByEmail(String email);

    /*@Query("SELECT s FROM Student s")
    List<Student> findAllStudents();

    @Query("SELECT t FROM Teacher t")
    List<Student> findAllTeachers();*/

    
}
