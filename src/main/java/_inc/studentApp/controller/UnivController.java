package _inc.studentApp.controller;

import _inc.studentApp.DTO.*;
import _inc.studentApp.complexKeys.DisciplinesKey;
import _inc.studentApp.model.*;
import _inc.studentApp.service.UnivService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class UnivController {

    // service
    private final UnivService service;


    // student methods
    @GetMapping("/get_students")
    public List<StudentDTO> findAllStudent(){
        return service.findAllStudent().stream().map(StudentDTO::fromEntity).collect(Collectors.toList());
    }
    @PostMapping("/save_student")
    public String saveStudent(@RequestBody Student student) {
        service.saveStudent(student);
        return "Student " + student.getEmail() + " successfully saved";
    }
    @GetMapping("/student-{email}")
    public StudentDTO findStudentByEmail(@PathVariable("email") String email) {
        return StudentDTO.fromEntity(service.findStudentByEmail(email));
    }
    @PutMapping("update_student")
    public StudentDTO updateStudent(@RequestBody Student student) {
        return StudentDTO.fromEntity(service.updateStudent(student));
    }

    @DeleteMapping("delete_student/{email}")
    public void deleteStudent(@PathVariable String email) {
        service.deleteStudent(email);
    }


    // employee methods
    @GetMapping("/get_employees")
    public List<EmployeeDTO> findAllEmployee(){
        return service.findAllEmployee().stream().map(EmployeeDTO::fromEntity).collect(Collectors.toList());
    }
    @PostMapping("/save_employee")
    public String saveEmployee(@RequestBody EmployeeRequest request) {
        Employee employee = service.saveEmployee(request);
        return "Student " + employee.getEmail() + " successfully saved";
    }
    @GetMapping("/employee-{email}")
    public Optional<Employee> findEmployeeByEmail(@PathVariable("email") String email) {
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

    // group methods
    @GetMapping("/get_groups")
    public List<Group> findAllGroup(){
        return service.findAllGroups();
    }
    @PostMapping("/save_group")
    public String saveGroup(@RequestBody Group group) {
        service.saveGroup(group);
        return "Group " + group.getGroupName() + " successfully saved";
    }
    @GetMapping("/group-{name}")
    public Optional<Group> findGroupByName(@PathVariable("name") String name) {
        return service.findByGroupName(name);
    }
    @PutMapping("update_group/{name}/{newName}")
    public Group updateGroup(@PathVariable("name") String name, @PathVariable("newName") String newName) {
        return service.updateGroup(name, newName);
    }
    @DeleteMapping("delete_group/{name}")
    public void deleteGroup(@PathVariable("name") String name) {
        service.deleteGroup(name);
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
    @GetMapping("/position-{name}")
    public Optional<Position> findPositionByName(@PathVariable("name") String name) {
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


    // discipline methods
    @GetMapping("/get_disciplines")
    public List<DisciplineDTO> findAllDisciplines(){
        return service.findAllDisciplines().stream().map(DisciplineDTO::fromEntity).collect(Collectors.toList());
    }
    @PostMapping("/save_discipline")
    public String saveDiscipline(@RequestBody DisciplineRequest request) {
        Disciplines created = service.saveDiscipline(request);
        return "Discipline " + created.getDisciplineName() + " successfully saved";
    }
    // TODO: DisciplineDTO vmesto Disciplines
    @GetMapping("/discipline_find")
    public DisciplineDTO findDisciplineByName(@RequestBody DisciplinesKey dk) {
        Disciplines discipline = service.findByDisciplineKey(dk).orElseThrow(() -> new EntityNotFoundException("Discipline not found"));
        return DisciplineDTO.fromEntity(discipline);
    }
    // TODO: DisciplineDTO vmesto Disciplines
    @PutMapping("update_discipline")
    public DisciplineDTO updateDiscipline(@RequestBody DisciplineUpdRequest request) {
        Disciplines disciplineUpd = service.updateDiscipline(request);
        return DisciplineDTO.fromEntity(disciplineUpd);
    }
    // TODO: DisciplineRequest vmesto Disciplines
    @DeleteMapping("delete_discipline")
    public void deleteDiscipline(@RequestBody DisciplineRequest request) {
        service.deleteDiscipline(request);
    }
}
