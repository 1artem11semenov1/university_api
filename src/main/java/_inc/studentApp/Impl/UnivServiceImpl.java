package _inc.studentApp.Impl;

import _inc.studentApp.model.Position;
import _inc.studentApp.model.Student;
import _inc.studentApp.model.Employee;
import _inc.studentApp.repository.PositionRepository;
import _inc.studentApp.repository.StudentRepository;
import _inc.studentApp.repository.EmployeeRepository;
import _inc.studentApp.service.UnivService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("myPersonService")
@AllArgsConstructor
@Primary
public class UnivServiceImpl implements UnivService {

    // repositories
    private final StudentRepository s_repository;
    private final EmployeeRepository e_repository;
    private final PositionRepository p_repository;

    // methods for students
    public List<Student> findAllStudent() {
        return s_repository.findAll();
    }
    @Override
    public Student saveStudent(Student student) {
        return s_repository.save(student);
    }
    @Override
    public Student findStudentByEmail(String email) {
        return s_repository.findByEmail(email);
    }
    @Override
    public Student updateStudent(Student student) {
        return s_repository.save(student);
    }
    @Transactional
    public void deleteStudent(String email) {
        s_repository.deleteByEmail(email);
    }

    // methods for employee
    public List<Employee> findAllEmployee() {
        return e_repository.findAll();
    }
    @Override
    public Employee saveEmployee(Employee employee) {
        return e_repository.save(employee);
    }
    @Override
    public Employee findEmployeeByEmail(String email) {
        return e_repository.findByEmail(email);
    }
    @Override
    public Employee updateEmployee(Employee employee) {
        return e_repository.save(employee);
    }
    @Transactional
    public void deleteEmployee(String email) {
        e_repository.deleteByEmail(email);
    }

    // methods for positions
    public List<Position> findAllPositions() {
        return p_repository.findAll();
    }
    @Override
    public Position savePosition(Position position) {
        return p_repository.save(position);
    }
    /*
    @Override
    public Position findPositionByID(Long id) {
        return p_repository.findById(id);
    }*/
    @Override
    public List<Position> findByPositionName(String positionName){
        return p_repository.findByPositionName(positionName);
    }
    @Override
    public Position updatePosition(Position position) {
        return p_repository.save(position);
    }
    @Transactional
    public void deletePosition(Long id) {
        p_repository.deleteById(id);
    }
}
