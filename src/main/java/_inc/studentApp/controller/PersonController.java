package _inc.studentApp.controller;

import _inc.studentApp.model.Person;
import _inc.studentApp.model.Student;
import _inc.studentApp.model.Teacher;
import _inc.studentApp.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class PersonController {

    private final PersonService service;

    @GetMapping("/get_students")
    public List<Student> findAllStudent(){
        return service.findAllStudent();
    }
    @GetMapping("/get_teachers")
    public List<Teacher> findAllTeacher(){
        return service.findAllTeacher();
    }

    @PostMapping("/save_student")
    public String saveStudent(@RequestBody Student student) {
        service.saveStudent(student);
        return "Student " + student.getEmail() + " successfully saved";
    }
    @PostMapping("/save_teacher")
    public String saveTeacher(@RequestBody Teacher teacher) {
        service.saveTeacher(teacher);
        return "Student " + teacher.getEmail() + " successfully saved";
    }

    @GetMapping("/student-{email}")
    public Student findStudentByEmail(@PathVariable("email") String email) {
        return service.findStudentByEmail(email);
    }
    @GetMapping("/teacher-{email}")
    public Teacher findTeacherByEmail(@PathVariable("email") String email) {
        return service.findTeacherByEmail(email);
    }

    @PutMapping("update_student")
    public Student updateStudent(@RequestBody Student student) { return service.updateStudent(student); }
    @PutMapping("update_teacher")
    public Teacher updateTeacher(@RequestBody Teacher teacher) {
        return service.updateTeacher(teacher);
    }

    @DeleteMapping("delete_student/{email}")
    public void deleteStudent(@PathVariable String email) {
        service.deleteStudent(email);
    }
    @DeleteMapping("delete_teacher/{email}")
    public void deleteTeacher(@PathVariable String email) {
        service.deleteTeacher(email);
    }
}
