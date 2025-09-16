package _inc.studentApp.service;

import _inc.studentApp.model.Person;
import _inc.studentApp.model.Student;
import _inc.studentApp.model.Teacher;

import java.util.List;

public interface PersonService {
    List<Student> findAllStudent();
    List<Teacher> findAllTeacher();

    Student saveStudent(Student student);
    Teacher saveTeacher(Teacher teacher);

    Student findStudentByEmail(String email);
    Teacher findTeacherByEmail(String email);

    Student updateStudent(Student student);
    Teacher updateTeacher(Teacher teacher);

    void deleteStudent(String email);
    void deleteTeacher(String email);
}
