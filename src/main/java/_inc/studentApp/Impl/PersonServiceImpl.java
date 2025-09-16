package _inc.studentApp.Impl;

import _inc.studentApp.model.Person;
import _inc.studentApp.model.Student;
import _inc.studentApp.model.Teacher;
import _inc.studentApp.repository.PersonRepository;
import _inc.studentApp.repository.StudentRepository;
import _inc.studentApp.repository.TeacherRepository;
import _inc.studentApp.service.PersonService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("myPersonService")
@AllArgsConstructor
@Primary
public class PersonServiceImpl implements PersonService {

    private final StudentRepository s_repository;
    private final TeacherRepository t_repository;

    public List<Student> findAllStudent() {
        return s_repository.findAll();
    }
    public List<Teacher> findAllTeacher() {
        return t_repository.findAll();
    }

    @Override
    public Student saveStudent(Student student) {
        return s_repository.save(student);
    }
    @Override
    public Teacher saveTeacher(Teacher teacher) {
        return t_repository.save(teacher);
    }

    @Override
    public Student findStudentByEmail(String email) {
        return s_repository.findByEmail(email);
    }
    @Override
    public Teacher findTeacherByEmail(String email) {
        return t_repository.findByEmail(email);
    }

    @Override
    public Student updateStudent(Student student) {
        return s_repository.save(student);
    }
    @Override
    public Teacher updateTeacher(Teacher teacher) {
        return t_repository.save(teacher);
    }

    @Transactional
    public void deleteStudent(String email) {
        s_repository.deleteByEmail(email);
    }
    @Transactional
    public void deleteTeacher(String email) {
        t_repository.deleteByEmail(email);
    }
}
