package _inc.studentApp.Impl;

import _inc.studentApp.DTO.*;
import _inc.studentApp.complexKeys.ClassRoomKey;
import _inc.studentApp.complexKeys.DisciplinesKey;
import _inc.studentApp.complexKeys.DistanceKey;
import _inc.studentApp.complexKeys.LessonKey;
import _inc.studentApp.model.*;
import _inc.studentApp.repository.*;
import _inc.studentApp.service.UnivService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private final UserRepository u_repository;
    private final UnitRepository un_repository;
    private final ClassRoomRepository c_repository;
    private final DistanceRepository dist_repository;
    private final LessonRepository l_repository;

    // pswd encoder
    PasswordEncoder encoder;

    // user methods
    @Override
    public User saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return u_repository.save(user);
    }

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

        List<Position> positions = new ArrayList<>();
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
        Disciplines discipline = new Disciplines(key, request.getCountHours(), group, teacher,null);
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
                request.getNewTeacherEmail(),
                request.getNewCountHours()
        );
        return saveDiscipline(newDisciplineRequest);
    }
    @Transactional
    public void deleteDiscipline(DisciplineRequest request) {
        DisciplinesKey dk = new DisciplinesKey(request.getDisciplineName(), request.getGroupName(), request.getTeacherEmail());
        d_repository.deleteById(dk);
    }

    // methods for units
    public List<Unit> findAllUnits() {
        return un_repository.findAll();
    }
    @Override
    public Unit saveUnit(Unit unit) {
        return un_repository.save(unit);
    }
    @Override
    public Optional<Unit> findUnitByName(String unitName) {
        return un_repository.findById(unitName);
    }
    @Override
    public Unit updateUnit(UnitUpdRequest request) {
        Unit old = un_repository.findById(request.getOldName()).orElseThrow();
        List<ClassRoom> classRooms = old.getClassrooms();
        List<Distance> distances = old.getDistances();
        un_repository.deleteById(request.getOldName());
        Unit newUnit = new Unit();
        newUnit.setUnitName(request.getNewName());
        newUnit.setAddress(request.getNewAddress());
        newUnit.setClassrooms(classRooms);
        newUnit.setDistances(distances);
        return un_repository.save(newUnit);
    }
    @Transactional
    public void deleteUnit(String unitName) {
        un_repository.deleteById(unitName);
    }

    // methods for classrooms
    public List<ClassRoom> findAllClassRooms() { return c_repository.findAll(); }
    @Override
    public ClassRoom saveClassRoom(ClassRoomRequest request) {
        ClassRoomKey key = new ClassRoomKey(request.getClassroomNumber(), request.getUnitName());
        Unit unit = un_repository.findById(request.getUnitName()).orElseThrow();
        ClassRoom newClassRoom = new ClassRoom();
        newClassRoom.setId(key);
        newClassRoom.setCapacity(request.getCapacity());
        newClassRoom.setUnit(unit);
        return c_repository.save(newClassRoom);
    }
    @Override
    public Optional<ClassRoom> findClassRoomByID(ClassRoomKey id) {
        return c_repository.findById(id);
    }
    @Override
    public ClassRoom updateClassRoom(ClassRoomUpdRequest request) {
        ClassRoom newClassRoom = new ClassRoom();
        ClassRoomKey key = new ClassRoomKey(request.getNewNumber(), request.getOld().getUnitName());
        Unit unit = un_repository.findById(request.getOld().getUnitName()).orElseThrow();
        newClassRoom.setId(key);
        newClassRoom.setCapacity(request.getNewCapacity());
        newClassRoom.setUnit(unit);
        c_repository.deleteById(request.getOld());
        return c_repository.save(newClassRoom);
    }
    @Transactional
    public void deleteClassRoom(ClassRoomKey id) {
        c_repository.deleteById(id);
    }

    // methods for distances
    public List<Distance> findAllDistances() {
        return dist_repository.findAll();
    }
    @Override
    public Distance saveDistance(DistanceRequest request) {
        Unit uf = un_repository.findById(request.getUnitFrom()).orElseThrow();
        Unit ut = un_repository.findById(request.getUnitTo()).orElseThrow();

        // save reverse distance
        DistanceRequest reverseDistance = new DistanceRequest(request.getUnitTo(), request.getUnitFrom(), request.getTime());
        dist_repository.save(reverseDistance.toEntity(ut));

        // save main distance
        return dist_repository.save(request.toEntity(uf));
    }
    @Override
    public Optional<Distance> findDistanceByID(DistanceKey id) {
        return dist_repository.findById(id);
    }
    @Override
    public Distance updateDistance(DistanceRequest request) { return saveDistance(request); }
    @Transactional
    public void deleteDistance(DistanceKey id) {
        // delete main
        dist_repository.deleteById(id);
        // delete reverse
        DistanceKey reverseKey = new DistanceKey(id.getUnitTo(), id.getUnitFrom());
        dist_repository.deleteById(reverseKey);
    }

    // lesson methods
    @Override
    public List<Lesson> findAllLessons() {
        return l_repository.findAll();
    }

    @Override
    public Lesson saveLesson(LessonRequest request) {
        Disciplines discipline = d_repository
                .findById(
                        new DisciplinesKey(
                                request.getDisciplineName(), request.getGroupName(), request.getTeacherEmail()
                        )
                ).orElseThrow();
        ClassRoom classRoom = c_repository
                .findById(
                        new ClassRoomKey(
                                request.getClassroomNumber(), request.getUnitName()
                        )
                ).orElseThrow();
        return l_repository.save(request.toEntity(discipline, classRoom));
    }

    @Override
    public Optional<Lesson> findLessonByID(LessonRequest request) {
        Disciplines discipline = d_repository
                .findById(
                        new DisciplinesKey(request.getDisciplineName(), request.getGroupName(), request.getTeacherEmail())
                ).orElseThrow();
        ClassRoom classRoom = c_repository
                .findById(
                        new ClassRoomKey(request.getClassroomNumber(), request.getUnitName())
                ).orElseThrow();

        return l_repository.findById(request.toEntity(discipline, classRoom).getId());
    }

    @Override
    public List<Lesson> findLessonsByDate(Date date) {
        return l_repository.findLessonsByDate(date);
    }

    @Override
    public Lesson updateLesson(LessonUpdRequest request) {
        //Lesson old = l_repository.findById(request.getOldId()).orElseThrow();
        Disciplines discipline = d_repository
                .findById(
                        new DisciplinesKey(request.getNewDisciplineName(), request.getNewGroupName(), request.getNewTeacherEmail())
                ).orElseThrow();
        ClassRoom classRoom = c_repository
                .findById(
                        new ClassRoomKey(request.getNewClassroomNumber(), request.getNewUnitName())
                ).orElseThrow();

        Lesson newLesson = new Lesson(request.getNewId(), classRoom, discipline);

        l_repository.deleteById(request.getOldId());
        return l_repository.save(newLesson);
    }

    @Override
    public void deleteLesson(LessonRequest request) {
        Lesson toDel = findLessonByID(request).orElseThrow();
        l_repository.deleteById(toDel.getId());
    }

    @Override
    public void deleteAllLessons() {
        l_repository.deleteAll();
    }

}
