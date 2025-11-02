package _inc.studentApp.controller;

import _inc.studentApp.DTO.*;
import _inc.studentApp.complexKeys.ClassRoomKey;
import _inc.studentApp.complexKeys.DisciplinesKey;
import _inc.studentApp.complexKeys.DistanceKey;
import _inc.studentApp.complexKeys.LessonKey;
import _inc.studentApp.model.*;
import _inc.studentApp.service.MyUserDetailsService;
import _inc.studentApp.service.UnivService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class UnivController {

    // service
    private final UnivService service;

    // security
    @GetMapping("/welcome")
    public String welcome(){
        return "welcome to the unprotected page";
    }
    @PostMapping("/new-user")
    public String addUser(@RequestBody User user){
        User newUser = service.saveUser(user);
        return "User " + newUser.getUserName() + " successfully saved";
    }

    // student methods
    @GetMapping("/get_students")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
    @PutMapping("/update_student")
    public StudentDTO updateStudent(@RequestBody Student student) {
        return StudentDTO.fromEntity(service.updateStudent(student));
    }

    @DeleteMapping("/delete_student/{email}")
    public void deleteStudent(@PathVariable String email) {
        service.deleteStudent(email);
    }


    // employee methods
    @GetMapping("/get_employees")
    //@PreAuthorize("hasAuthority('ROLE_TEACHER')")
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
    @PutMapping("/update_employee")
    public Employee updateEmployee(@RequestBody Employee employee) {
        return service.updateEmployee(employee);
    }
    @DeleteMapping("/delete_employee/{email}")
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
    @PutMapping("/update_group/{name}/{newName}")
    public Group updateGroup(@PathVariable("name") String name, @PathVariable("newName") String newName) {
        return service.updateGroup(name, newName);
    }
    @DeleteMapping("/delete_group/{name}")
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
    @PutMapping("/update_position")
    public Position updatePosition(@RequestBody Position position) {
        return service.updatePosition(position);
    }
    @DeleteMapping("/delete_position/{id}")
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
    @GetMapping("/discipline_find")
    public DisciplineDTO findDisciplineByName(@RequestBody DisciplinesKey dk) {
        Disciplines discipline = service.findByDisciplineKey(dk).orElseThrow(() -> new EntityNotFoundException("Discipline not found"));
        return DisciplineDTO.fromEntity(discipline);
    }
    @PutMapping("/update_discipline")
    public DisciplineDTO updateDiscipline(@RequestBody DisciplineUpdRequest request) {
        Disciplines disciplineUpd = service.updateDiscipline(request);
        return DisciplineDTO.fromEntity(disciplineUpd);
    }
    @DeleteMapping("/delete_discipline")
    public void deleteDiscipline(@RequestBody DisciplineRequest request) {
        service.deleteDiscipline(request);
    }

    // units methods
    @GetMapping("/get_units")
    public List<Unit> findAllUnits(){
        return service.findAllUnits();
    }
    @PostMapping("/save_unit")
    public String saveUnit(@RequestBody Unit unit) {
        service.saveUnit(unit);
        return "Unit " + unit.getUnitName() + " at address " + unit.getAddress() + " successfully saved";
    }
    @GetMapping("/unit-{name}")
    public Optional<Unit> findUnitByName(@PathVariable("name") String name) {
        return service.findUnitByName(name);
    }
    @PutMapping("/update_unit")
    public Unit updateUnit(@RequestBody UnitUpdRequest request) {
        return service.updateUnit(request);
    }
    @DeleteMapping("/delete_unit/{name}")
    public void deleteUnit(@PathVariable String name) {
        service.deleteUnit(name);
    }

    // classroom methods
    @GetMapping("/get_classrooms")
    public List<ClassRoomRequest> findAllClassRooms(){
        return service.findAllClassRooms().stream().map(ClassRoomRequest::fromEntity).collect(Collectors.toList());
    }
    @PostMapping("/save_classroom")
    public String saveClassRoom(@RequestBody ClassRoomRequest request) {
        ClassRoom created = service.saveClassRoom(request);
        return "ClassRoom " + created.getId().getClassroomNumber() + " in unit " + created.getId().getUnitName() + " successfully saved";
    }
    @GetMapping("/classroom_find")
    public ClassRoomRequest findClassRoomById(@RequestBody ClassRoomKey key) {
        ClassRoom classRoom = service.findClassRoomByID(key).orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
        return ClassRoomRequest.fromEntity(classRoom);
    }
    @PutMapping("/update_classroom")
    public ClassRoomRequest updateClassRoom(@RequestBody ClassRoomUpdRequest request) {
        ClassRoom classRoomUpd = service.updateClassRoom(request);
        return ClassRoomRequest.fromEntity(classRoomUpd);
    }
    @DeleteMapping("/delete_classroom")
    public void deleteClassRoom(@RequestBody ClassRoomKey key) {
        service.deleteClassRoom(key);
    }

    // distance methods
    @GetMapping("/get_distances")
    public List<DistanceRequest> findAllDistances(){
        return service.findAllDistances().stream().map(DistanceRequest::fromEntity).collect(Collectors.toList());
    }
    @PostMapping("/save_distance")
    public String saveDistance(@RequestBody DistanceRequest request) {
        Distance created = service.saveDistance(request);
        return "Distance from " + created.getId().getUnitFrom() + " to " + created.getId().getUnitTo() + " successfully saved";
    }
    @GetMapping("/distance_find")
    public DistanceRequest findDistanceById(@RequestBody DistanceKey key) {
        Distance dist = service.findDistanceByID(key).orElseThrow(() -> new EntityNotFoundException("Distance not found"));
        return DistanceRequest.fromEntity(dist);
    }
    @PutMapping("/update_distance")
    public DistanceRequest updateDistance(@RequestBody DistanceRequest request) {
        Distance distUpd = service.updateDistance(request);
        return DistanceRequest.fromEntity(distUpd);
    }
    @DeleteMapping("/delete_distance")
    public void deleteDistance(@RequestBody DistanceKey key) {
        service.deleteDistance(key);
    }

    // lesson methods
    @GetMapping("/get_lessons")
    public List<LessonRequest> findAllLessons(){
        return service.findAllLessons().stream().map(LessonRequest::fromEntity).collect(Collectors.toList());
    }
    @PostMapping("/save_lesson")
    public String saveLesson(@RequestBody LessonRequest request) {
        String createLog = service.saveLesson(request);
        if (!createLog.startsWith("Lesson not added.")) {
            return "Lesson of " + request.getDisciplineName()
                    + " for group " + request.getGroupName()
                    + " with teacher " + request.getTeacherEmail()
                    + " in classroom " + request.getClassroomNumber()
                    + " successfully saved!\n"
                    + createLog;
        }
        return createLog;
    }
    @GetMapping("/lesson_find")
    public LessonRequest findLessonById(@RequestBody LessonRequest request) {
        Lesson lesson = service.findLessonByID(request).orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
        return LessonRequest.fromEntity(lesson);
    }
    @GetMapping("/find_lesson_ondate")
    public List<LessonRequest> findLessonsByDate(@RequestBody DateRequest request){
        return service.findLessonsByDate(request.getDate()).stream().map(LessonRequest::fromEntity).collect(Collectors.toList());
    }
    @PutMapping("/update_lesson")
    public String updateLesson(@RequestBody LessonUpdRequest request) {
        String createLog = service.updateLesson(request);
        if (!createLog.startsWith("Cannot update lesson with causes:")) {
            return "Lesson of " + request.getNewDisciplineName()
                    + " for group " + request.getNewGroupName()
                    + " with teacher " + request.getNewTeacherEmail()
                    + " in classroom " + request.getNewClassroomNumber()
                    + " successfully updated!\n"
                    + createLog;
        }
        return createLog;
    }
    @DeleteMapping("/delete_lesson")
    public void deleteLesson(@RequestBody LessonRequest request) {
        service.deleteLesson(request);
    }
    @DeleteMapping("/delete_all_lessons")
    public void deleteAllLessons() {
        service.deleteAllLessons();
    }

}
