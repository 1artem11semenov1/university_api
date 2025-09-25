package _inc.studentApp.controller;

import _inc.studentApp.model.Position;
import _inc.studentApp.model.Student;
import _inc.studentApp.model.Employee;
import _inc.studentApp.service.UnivService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class UnivController {

    // service
    private final UnivService service;


    // student methods
    @GetMapping("/get_students")
    public List<Student> findAllStudent(){
        return service.findAllStudent();
    }
    @PostMapping("/save_student")
    public String saveStudent(@RequestBody Student student) {
        service.saveStudent(student);
        return "Student " + student.getEmail() + " successfully saved";
    }
    @GetMapping("/student-{email}")
    public Student findStudentByEmail(@PathVariable("email") String email) {
        return service.findStudentByEmail(email);
    }
    @PutMapping("update_student")
    public Student updateStudent(@RequestBody Student student) { return service.updateStudent(student); }

    @DeleteMapping("delete_student/{email}")
    public void deleteStudent(@PathVariable String email) {
        service.deleteStudent(email);
    }


    // employee methods
    @GetMapping("/get_employees")
    public List<Employee> findAllEmployee(){
        return service.findAllEmployee();
    }
    @PostMapping("/save_employee")
    public String saveEmployee(@RequestBody Employee employee) {
        service.saveEmployee(employee);
        return "Student " + employee.getEmail() + " successfully saved";
    }
    @GetMapping("/employee-{email}")
    public Employee findEmployeeByEmail(@PathVariable("email") String email) {
        return service.findEmployeeByEmail(email);
    }
    @PutMapping("update_employee")
    public Employee updateEmployee(@RequestBody Employee employee) {
        return service.updateEmployee(employee);
    }
    @DeleteMapping("delete_employee/{email}")
    public void deleteEmployee(@PathVariable String email) {
        service.deleteEmployee(email);
    }

    // position methods
    @GetMapping("/get_positions")
    public List<Position> findAllPosition(){
        return service.findAllPositions();
    }
    @PostMapping("/save_position")
    public String savePosition(@RequestBody Position position) {
        service.savePosition(position);
        return "Student " + position.getPositionName() + " successfully saved";
    }
    /*@GetMapping("/position-{ID}")
    public Position findPositionById(@PathVariable("ID") Long id) {
        return service.findPositionByID(id);
    }*/
    @GetMapping("/position-{name}")
    public List<Position> findPositionByName(@PathVariable("name") String name) {
        return service.findByPositionName(name);
    }
    @PutMapping("update_position")
    public Position updatePosition(@RequestBody Position position) {
        return service.updatePosition(position);
    }
    @DeleteMapping("delete_position/{id}")
    public void deletePosition(@PathVariable Long id) {
        service.deletePosition(id);
    }
}
