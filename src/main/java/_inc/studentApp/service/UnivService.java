package _inc.studentApp.service;

import _inc.studentApp.DTO.*;
import _inc.studentApp.complexKeys.ClassRoomKey;
import _inc.studentApp.complexKeys.DisciplinesKey;
import _inc.studentApp.complexKeys.DistanceKey;
import _inc.studentApp.complexKeys.LessonKey;
import _inc.studentApp.model.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UnivService {

    // user methods
    List<User> findAllUsers();
    String saveUser(User user);
    User findUserByUserName(String username);
    String updateUser(User user);
    void deleteUser(String username);
    void deleteAllUsers();

    // student methods
    List<Student> findAllStudent();
    Student saveStudent(Student student);
    Student findStudentByEmail(String email);
    String updateStudent(Student student);
    void deleteStudent(String email);


    // employee methods
    Employee saveEmployee(EmployeeRequest request);
    List<Employee> findAllEmployee();
    Optional<Employee> findEmployeeByEmail(String email);
    String updateEmployee(EmployeeRequest request);
    void deleteEmployee(String email);


    // positions methods
    Position savePosition(Position position);
    List<Position> findAllPositions();
    Optional<Position> findByPositionName(String positionName);
    String updatePosition(String name, String newName);
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
    String updateDiscipline(DisciplineUpdRequest request);
    void deleteDiscipline(DisciplinesKey key);

    // unit methods
    List<Unit> findAllUnits();
    Unit saveUnit(Unit unit);
    Optional<Unit> findUnitByName(String unitName);
    String updateUnit(UnitUpdRequest unit);
    void deleteUnit(String unitName);

    // classroom methods
    List<ClassRoom> findAllClassRooms();
    ClassRoom saveClassRoom(ClassRoomRequest request);
    Optional<ClassRoom> findClassRoomByID(ClassRoomKey id);
    String updateClassRoom(ClassRoomUpdRequest unit);
    void deleteClassRoom(ClassRoomKey id);

    // distance methods
    List<Distance> findAllDistances();
    Distance saveDistance(DistanceRequest request);
    Optional<Distance> findDistanceByID(DistanceKey id);
    String updateDistance(DistanceRequest request);
    void deleteDistance(DistanceKey id);

    // lesson methods
    List<Lesson> findAllLessons();
    String saveLesson(LessonRequest request);
    Optional<Lesson> findLessonByID(LessonRequest request);
    List<Lesson> findLessonsByDate(Date date);
    String updateLesson(LessonUpdRequest request);
    void deleteLesson(LessonRequest request);
    void deleteAllLessons();
}
