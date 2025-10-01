package _inc.studentApp.service;

import _inc.studentApp.DTO.DisciplineRequest;
import _inc.studentApp.DTO.DisciplineUpdRequest;
import _inc.studentApp.DTO.EmployeeRequest;
import _inc.studentApp.complexKeys.DisciplinesKey;
import _inc.studentApp.model.*;

import java.util.List;
import java.util.Optional;

public interface UnivService {

    // student methods
    List<Student> findAllStudent();
    Student saveStudent(Student student);
    Student findStudentByEmail(String email);
    Student updateStudent(Student student);
    void deleteStudent(String email);


    // employee methods
    Employee saveEmployee(EmployeeRequest request);
    List<Employee> findAllEmployee();
    Optional<Employee> findEmployeeByEmail(String email);
    Employee updateEmployee(Employee employee);
    void deleteEmployee(String email);


    // positions methods
    Position savePosition(Position position);
    List<Position> findAllPositions();
    Optional<Position> findByPositionName(String positionName);
    Position updatePosition(Position position);
    void deletePosition(Long Id);

    // group methods
    Group saveGroup(Group group);
    List<Group> findAllGroups();
    Optional<Group> findByGroupName(String groupName);
    Group updateGroup(String name, String group);
    void deleteGroup(String groupName);

    // discipline methods
    Disciplines saveDiscipline(DisciplineRequest request);
    List<Disciplines> findAllDisciplines();
    Optional<Disciplines> findByDisciplineKey(DisciplinesKey dk);
    Disciplines updateDiscipline(DisciplineUpdRequest request);
    void deleteDiscipline(DisciplineRequest request);
}
