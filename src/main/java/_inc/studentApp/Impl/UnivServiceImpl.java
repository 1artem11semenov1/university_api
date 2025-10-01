package _inc.studentApp.Impl;

import _inc.studentApp.DTO.DisciplineRequest;
import _inc.studentApp.DTO.DisciplineUpdRequest;
import _inc.studentApp.DTO.EmployeeRequest;
import _inc.studentApp.complexKeys.DisciplinesKey;
import _inc.studentApp.model.*;
import _inc.studentApp.repository.*;
import _inc.studentApp.service.UnivService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service("myPersonService")
@AllArgsConstructor
@Primary
public class UnivServiceImpl implements UnivService {

    // repositories
    private final StudentRepository s_repository;
    private final EmployeeRepository e_repository;
    private final PositionRepository p_repository;
    private final GroupRepository g_repository;
    private final DisciplineRepository d_repository;

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
    public Employee saveEmployee(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setEmail(request.getEmail());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setDateOfBirth(request.getDateOfBirth());
        employee.setExperience(request.getExperience());

        Set<Position> positions = new HashSet<>();
        for (String posName : request.getPositions()){
            Position pos = p_repository.findByPositionName(posName)
                    .orElseThrow(() -> new EntityNotFoundException("Position not added"));
            positions.add(pos);
        }

        employee.setPositions(positions);

        return e_repository.save(employee);
    }
    @Override
    public Optional<Employee> findEmployeeByEmail(String email) {
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
    @Override
    public Optional<Position> findByPositionName(String positionName){
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

    // methods for group
    public List<Group> findAllGroups() {
        return g_repository.findAll();
    }
    @Override
    public Group saveGroup(Group group) {
        return g_repository.save(group);
    }
    @Override
    public Optional<Group> findByGroupName(String groupName){
        return g_repository.findByGroupName(groupName);
    }
    @Override
    public Group updateGroup(String name, String newName) {
        //return g_repository.save(name, group);
        return g_repository.update(name, newName);
    }
    @Transactional
    public void deleteGroup(String groupName) {
        g_repository.deleteByGroupName(groupName);
    }

    // methods for disciplines
    public List<Disciplines> findAllDisciplines() {
        return d_repository.findAll();
    }
    @Override
    public Disciplines saveDiscipline(DisciplineRequest request) {
        Employee teacher = e_repository.findByEmail(request.getTeacherEmail())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));
        Group group = g_repository.findByGroupName(request.getGroupName())
                .orElseThrow(() -> new EntityNotFoundException("Teacher not found"));

        DisciplinesKey key = new DisciplinesKey(request.getDisciplineName(), group.getGroupName(), teacher.getEmail());
        Disciplines discipline = new Disciplines(key, group, teacher);
        return d_repository.save(discipline);
    }
    @Override
    public Optional<Disciplines> findByDisciplineKey(DisciplinesKey dk){ return d_repository.findById(dk); }
    @Override
    public Disciplines updateDiscipline(DisciplineUpdRequest request) {
        DisciplinesKey old = new DisciplinesKey(
                request.getDisciplineName(),
                request.getGroupName(),
                request.getTeacherEmail()
        );
        d_repository.deleteById(old);
        DisciplineRequest newDisciplineRequest = new DisciplineRequest(
                request.getNewDisciplineName(),
                request.getNewGroupName(),
                request.getNewTeacherEmail()
        );
        return saveDiscipline(newDisciplineRequest);
    }
    @Transactional
    public void deleteDiscipline(DisciplineRequest request) {
        DisciplinesKey dk = new DisciplinesKey(request.getDisciplineName(), request.getGroupName(), request.getTeacherEmail());
        d_repository.deleteById(dk);
    }
}
