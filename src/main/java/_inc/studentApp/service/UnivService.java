package _inc.studentApp.service;

import _inc.studentApp.model.Position;
import _inc.studentApp.model.Student;
import _inc.studentApp.model.Employee;

import java.util.List;

public interface UnivService {
    List<Student> findAllStudent();
    Student saveStudent(Student student);
    Student findStudentByEmail(String email);
    Student updateStudent(Student student);
    void deleteStudent(String email);

    Employee saveEmployee(Employee employee);
    List<Employee> findAllEmployee();
    Employee findEmployeeByEmail(String email);
    Employee updateEmployee(Employee employee);
    void deleteEmployee(String email);

    Position savePosition(Position position);
    List<Position> findAllPositions();
    //Position findPositionByID(Long Id);
    List<Position> findByPositionName(String positionName);
    Position updatePosition(Position position);
    void deletePosition(Long Id);
}
