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

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service("myUnivService")
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
    public List<User> findAllUsers(){
        return u_repository.findAll();
    }
    @Override
    public String saveUser(User user) {
        String ret = "";

        if (user.getUserName().equals("INIT_ADMIN") && u_repository.findByUserName("INIT_ADMIN").isEmpty()){
            user.setPassword(encoder.encode(user.getPassword()));
            u_repository.save(user);
            return "Default admin user successfully created.";
        }

        boolean isNormal = true;

        List<String> inputRole = Arrays.stream(user.getRole().split(" ")).toList();
        if (inputRole.contains("ROLE_ADMIN") || inputRole.contains("ROLE_TEACHER")){
            Optional<Employee> employee = e_repository.findByEmail(user.getEmail());
            if (employee.isPresent()){
                user.setEmployee(employee.get());
                List<String> positionsNames = employee.get().getPositions()
                        .stream().map(Position::getPositionName)
                        .toList();
                for (String role : inputRole){
                    if (role.equals("ROLE_ADMIN") && !positionsNames.contains("admin")){
                        isNormal = false;
                        break;
                    } else if (role.equals("ROLE_TEACHER") && !positionsNames.contains("teacher")){
                        isNormal = false;
                        break;
                    }
                }
            } else {
                isNormal = false;
            }

            if (!isNormal){
                ret += "\nThere is no employee "
                        + user.getEmail()
                        + " or the entered role does not correspond to the positions saved for this employee.";
            }
        }

        if (inputRole.contains("ROLE_STUDENT")){
            Student student = s_repository.findByEmail(user.getEmail());
            if (student == null){
                isNormal = false;
                ret += "\nThere is no student "
                        + user.getEmail() + ".";
            } else {
                user.setStudent(student);
            }
        }
        if (isNormal) {
            user.setPassword(encoder.encode(user.getPassword()));
            u_repository.save(user);
            ret = "User " + user.getUserName() + " successfully added.";
        } else {
            ret = "User " + user.getUserName() + " not added." + ret;
        }
        return ret;
    }
    @Override
    public User findUserByUserName(String username){
        return u_repository.findByUserName(username).orElseThrow();
    }
    public String updateUser(User user){
        String ret = "";
        Optional<User> old = u_repository.findByUserName(user.getUserName());
        if (old.isEmpty()){
            ret = "Can not update " + user.getUserName() + " because user not exist.";
            return ret;
        }

        User tmp = old.get();
        u_repository.deleteById(old.get().getUserId());
        String saveLog = saveUser(user);
        if (! saveLog.contains("successfully added")){
            u_repository.save(tmp);
            ret = "Can not update " + user.getUserName() + "\n" + saveLog;
        }

        return ret;
    }
    @Override
    public void deleteUser(String username){
        Optional<User> toDel = u_repository.findByUserName(username);
        toDel.ifPresent(user -> u_repository.deleteById(user.getUserId()));
    }
    public void deleteAllUsers(){
        u_repository.deleteAll();
    }

    // methods for students
    public List<Student> findAllStudent() {
        return s_repository.findAll();
    }
    @Override
    public Student saveStudent(Student student) {
        Group group = g_repository.findByGroupName(student.getGroup().getGroupName()).orElseThrow();
        student.setGroup(group);
        return s_repository.save(student);
    }
    @Override
    public Student findStudentByEmail(String email) {
        return s_repository.findByEmail(email);
    }
    @Override
    public String updateStudent(Student student) {
        Group group = g_repository.findByGroupName(student.getGroup().getGroupName()).orElseThrow();
        student.setGroup(group);
        Student old = s_repository.findByEmail(student.getEmail());
        if (old == null){
            return "There is no student " + student.getEmail();
        }

        student.setId(old.getId());
        s_repository.save(student);
        return "Student " + student.getEmail() + " successfully updated";
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
    public String updateEmployee(EmployeeRequest request) {
        Optional<Employee> old = findEmployeeByEmail(request.getEmail());
        if (old.isEmpty()){
            return "There is no employee " + request.getEmail();
        }
        saveEmployee(request);
        return "Employee " + request.getEmail() + " successfully updated.";
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
    public String updatePosition(String name, String newName) {
        Optional<Position> old = p_repository.findByPositionName(name);
        if (old.isEmpty()){
            return "There is no position " + name;
        }
        old.get().setPositionName(newName);
        p_repository.save(old.get());
        return "Position " + name + " successfully updated to " + newName;
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
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        Disciplines discipline = new Disciplines();
        discipline.setWithoutId(
                request.getDisciplineName(),
                group.getId(),
                teacher.getId(),
                request.getCountHours(),
                group,
                teacher,
                null
        );

        Optional<Disciplines> check = d_repository.findByPotentialKey(request.getDisciplineName(), group.getId(), teacher.getId());
        if (check.isEmpty()) {
            return d_repository.save(discipline);
        }

        return check.get();
    }
    @Override
    public Optional<Disciplines> findByDisciplineKey(DisciplinesKey dk){
        return d_repository.findByPotentialKey(dk.getDisciplineName(), dk.getGroupId(), dk.getTeacherId());
    }
    @Override
    public String updateDiscipline(DisciplineUpdRequest request) {
        Optional<Group> oldGroup = g_repository.findByGroupName(request.getGroupName());
        Optional<Employee> oldEmployee = e_repository.findByEmail(request.getTeacherEmail());
        if (oldGroup.isPresent() && oldEmployee.isPresent()) {
            DisciplinesKey oldKey = new DisciplinesKey(
                    request.getDisciplineName(),
                    oldGroup.get().getId(),
                    oldEmployee.get().getId()
                    );
            Optional<Disciplines> old = d_repository.findByPotentialKey(oldKey.getDisciplineName(), oldKey.getGroupId(), oldKey.getTeacherId());
            if (old.isEmpty()) {
                return "There is no discipline " + request.getDisciplineName();
            }
            Optional<Group> newGroup = g_repository.findByGroupName(request.getGroupName());
            Optional<Employee> newTeacher = e_repository.findByEmail(request.getNewTeacherEmail());
            if (newTeacher.isEmpty() || newGroup.isEmpty()) {
                String log = "";
                if (newTeacher.isEmpty()) {
                    log += "Can not update. Teacher " + request.getNewTeacherEmail() + " not exist ";
                }
                if (newGroup.isEmpty()) {
                    log += "Can not update. Group " + request.getNewGroupName() + " not exist";
                }
                return log;
            } else {
                Disciplines newDis = d_repository.update(
                        request.getDisciplineName(),
                        oldGroup.get().getId(),
                        oldEmployee.get().getId(),
                        request.getNewDisciplineName(),
                        newGroup.get().getId(),
                        newTeacher.get().getId(),
                        request.getNewCountHours()
                );

                return "Successfully update discipline " + newDis.getDisciplineName();
            }
        }
        return "Old group or employee not exist";
    }
    @Override
    @Transactional
    public void deleteDiscipline(DisciplinesKey key) {
        Optional<Disciplines> toDelete = d_repository.findByPotentialKey(key.getDisciplineName(), key.getGroupId(), key.getTeacherId());
        toDelete.ifPresent(disciplines -> d_repository.deleteById(disciplines.getId()));
    }

    // methods for units
    @Override
    public List<Unit> findAllUnits() {
        return un_repository.findAll();
    }
    @Override
    public Unit saveUnit(Unit unit) {
        Optional<Unit> check = un_repository.findByUnitName(unit.getUnitName());
        if (check.isEmpty()) {
            return un_repository.save(unit);
        }
        return unit;
    }
    @Override
    public Optional<Unit> findUnitByName(String unitName) {
        return un_repository.findByUnitName(unitName);
    }
    @Override
    public String updateUnit(UnitUpdRequest request) {
        Optional<Unit> old = un_repository.findByUnitName(request.getOldName());
        if (old.isEmpty()){
            return "There is no unit " + request.getOldName();
        }
        un_repository.update(
                request.getOldName(),
                request.getNewName(),
                request.getNewAddress()
        );

        return "Successfully update unit " + request.getOldName()
                + " to name " + request.getNewName()
                + " at address " + request.getNewAddress();
    }
    @Transactional
    public void deleteUnit(String unitName) {
        Optional<Unit> toDel = un_repository.findByUnitName(unitName);
        toDel.ifPresent(unit -> un_repository.deleteById(unit.getId()));
    }

    // methods for classrooms
    public List<ClassRoom> findAllClassRooms() { return c_repository.findAll(); }
    @Override
    public ClassRoom saveClassRoom(ClassRoomRequest request) {
        ClassRoomKey key = new ClassRoomKey(request.getClassroomNumber(), request.getUnitID());
        Unit unit = un_repository.findById(request.getUnitID()).orElseThrow();

        Optional<ClassRoom> check = c_repository.findByPotentialKey(key.getClassroomNumber(), key.getUnitID());
        if (check.isEmpty()) {
            ClassRoom newClassRoom = new ClassRoom();
            newClassRoom.setClassroomNumber(request.getClassroomNumber());
            newClassRoom.setUnitID(unit.getId());
            newClassRoom.setCapacity(request.getCapacity());
            newClassRoom.setUnit(unit);
            return c_repository.save(newClassRoom);
        }
        return check.get();
    }
    @Override
    public Optional<ClassRoom> findClassRoomByID(ClassRoomKey id) {
        return c_repository.findByPotentialKey(id.getClassroomNumber(), id.getUnitID());
    }
    @Override
    public String updateClassRoom(ClassRoomUpdRequest request) {
        Optional<ClassRoom> old = c_repository.findByPotentialKey(request.getOld().getClassroomNumber(), request.getOld().getUnitID());
        if (old.isEmpty()){
            return "There is no classroom " + request.getOld().getClassroomNumber() + " in unit " + request.getOld().getUnitID();
        }

        c_repository.update(
                old.get().getId(),
                request.getOld().getClassroomNumber(),
                request.getNewNumber(),
                request.getNewCapacity()
        );
        return "Classroom " + request.getNewNumber() + " successfully update";
    }
    @Transactional
    public void deleteClassRoom(ClassRoomKey id) {
        Optional<ClassRoom> toDel = c_repository.findByPotentialKey(id.getClassroomNumber(), id.getUnitID());
        toDel.ifPresent(classroom -> c_repository.deleteById(toDel.get().getId()));
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
        dist_repository.save(reverseDistance.toEntity(ut, uf));

        // save main distance
        return dist_repository.save(request.toEntity(uf, ut));
    }
    @Override
    public Optional<Distance> findDistanceByID(DistanceKey id) {
        return dist_repository.findById(id);
    }
    @Override
    public String updateDistance(DistanceRequest request) {
        Optional<Unit> unitF = un_repository.findById(request.getUnitFrom());
        if (unitF.isEmpty()){
            return "There is no unit " + request.getUnitFrom();
        }
        Optional<Unit> unitT = un_repository.findById(request.getUnitTo());
        if (unitT.isEmpty()){
            return "There is no unit " + request.getUnitTo();
        }

        saveDistance(request);
        return "Distance from " + request.getUnitFrom() + " to " + request.getUnitTo() + " successfully updated";
    }
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
    public String saveLesson(LessonRequest request) {
        Optional<Disciplines> discipline = d_repository.findById(request.getDisciplineID());
        Optional<ClassRoom> classRoom = c_repository.findById(request.getClassRoomID());

        if (discipline.isPresent() && classRoom.isPresent()) {
            String isNormal = isNormalLesson(discipline.get(), classRoom.get(), request);

            if (isNormalLessonCheckerAnswer(isNormal)) {
                l_repository.save(request.toEntity(discipline.get(), classRoom.get()));
            } else {
                isNormal = "Lesson not added." + isNormal;
            }

            return isNormal;
        }
        return "Lesson not added. Discipline or classroom not exist";
    }

    @Override
    public Optional<Lesson> findLessonByID(LessonRequest request) {
        Optional<Disciplines> discipline = d_repository.findById(request.getDisciplineID());
        Optional<ClassRoom> classRoom = c_repository.findById(request.getClassRoomID());
        if (discipline.isPresent() && classRoom.isPresent()) {
            return l_repository.findById(request.toEntity(discipline.get(), classRoom.get()).getId());
        }
        return Optional.empty();
    }

    @Override
    public List<Lesson> findLessonsByDate(Date date) {
        return l_repository.findLessonsByDate(date);
    }

    @Override
    public String updateLesson(LessonUpdRequest request) {
        Optional<Disciplines> discipline = d_repository.findById(request.getNewDisciplineID());
        Optional<ClassRoom> classRoom = c_repository.findById(request.getNewClassRoomID());

        if (discipline.isPresent() && classRoom.isPresent()) {
            Lesson newLesson = new Lesson(request.getNewId(), classRoom.get(), discipline.get());

            String isNormal = isNormalLesson(
                    discipline.get(),
                    classRoom.get(),
                    new LessonRequest(
                            request.getNewDisciplineID(),
                            request.getNewClassRoomID(),
                            request.getNewDate())
            );

            Lesson tmpOld = l_repository.findById(request.getOldId()).orElseThrow();
            l_repository.deleteById(request.getOldId());
            if (isNormalLessonCheckerAnswer(isNormal)) {
                l_repository.save(newLesson);
            } else {
                l_repository.save(tmpOld);
                isNormal = "Cannot update lesson with causes:" + isNormal;
            }

            return isNormal;
        }
        return "Cannot update lesson with causes: Classroom or discipline not exist";
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

    // checker for lesson add
    // checks that classroom, unit, count hours at week is normal and lessons is not embedded
    private String isNormalLesson(Disciplines discipline, ClassRoom classRoom, LessonRequest request){
        String retVal = "";

        // is normal classroom capacity
        Group group = discipline.getGroup();
        int countStudents = group.getStudents().size();
        if (countStudents > classRoom.getCapacity()){
            retVal += "\nClassroom "
                    + classRoom.getClassroomNumber()
                    + " has insufficient capacity for group "
                    + group.getGroupName();
        }

        // is groups intersection
        List<Lesson> lessonsAtDate = l_repository.findLessonsByDate(request.getDate());
        int countStudentsGlobal = countStudents;
        int countGroups = 1;
        ClassRoom curClassroom;
        Disciplines curDiscipline;
        for (Lesson l : lessonsAtDate){
            curClassroom = c_repository.findById(l.getId().getClassroom()).orElseThrow();
            curDiscipline = d_repository.findById(l.getId().getDiscipline()).orElseThrow();
            if (l.getId().getDate().compareTo(request.getDate()) == 0
                    && curClassroom.getClassroomNumber().equals(classRoom.getClassroomNumber())
                    && !( (group.getGroupName()) .equals(curDiscipline.getGroup().getGroupName()) ) ){
                countStudentsGlobal += curDiscipline.getGroup().getStudents().size();
                ++ countGroups;
            }
        }
        if (countStudentsGlobal != countStudents){
            retVal += "\n Classroom "
                    + classRoom.getClassroomNumber()
                    + " has insufficient capacity for "
                    + countGroups + " groups ";
        }

        // is normal time
        for (Lesson l : lessonsAtDate){
            curDiscipline = d_repository.findById(l.getId().getDiscipline()).orElseThrow();
            if ( (curDiscipline.getGroup().getGroupName()) .equals(group.getGroupName())
                && (l.getId().getDate()) .compareTo(request.getDate()) == 0 ){
                retVal += "\n There is already a lesson for group "
                        + group.getGroupName()
                        + " at this time.";
                break;
            }
        }

        // is normal unit
        // get all lessons for this group at this date
        List<Lesson> lessonAtDateAddingGroup = new ArrayList<>();
        for (Lesson l : lessonsAtDate){
            curDiscipline = d_repository.findById(l.getId().getDiscipline()).orElseThrow();
            if ( (curDiscipline.getGroup().getGroupName()) .equals(group.getGroupName()) ){
                lessonAtDateAddingGroup.add(l);
            }
        }
        // find previous lesson
        Lesson prevLesson = getPrevious(lessonAtDateAddingGroup, request.getDate());
        if (prevLesson != null) {
            curClassroom = c_repository.findById(prevLesson.getId().getClassroom()).orElseThrow();
            // check transfer time
            if (!(curClassroom.getUnit().getUnitName().equals(classRoom.getUnit().getUnitName()))) {
                Duration timeToPrevLesson = getPositiveDuration(
                        request.getDate().toInstant(),
                        prevLesson.getId().getDate().toInstant()
                );

                long minutesToPrevious = timeToPrevLesson.toMinutes() - 90; // -90 for get end of lesson
                Distance distance = dist_repository.findById(
                        new DistanceKey(
                                c_repository.findById(request.getClassRoomID()).orElseThrow().getUnitID(),
                                prevLesson.getClassroom().getUnit().getId()
                        )
                ).orElseThrow();
                long timeBetweenUnits = distance.getTimeMinutes();

                if (timeBetweenUnits > minutesToPrevious) {
                    retVal += "\nTime between units "
                            + classRoom.getUnit().getUnitName()
                            + " and "
                            + prevLesson.getClassroom().getUnit().getUnitName()
                            + " is too long. Choose other unit for this class.";
                }
            }
        }

        // is normal count hours
        // get count hours at week
        double countHoursAtSemester = discipline.getCountHours();
        double countWeeksAtSemester = 16;
        int countHoursAtWeek = Math.toIntExact(Math.round(countHoursAtSemester / countWeeksAtSemester));

        // get monday and sunday
        LocalDate curDate = request.getDate().toInstant()
                .atZone(ZoneId.of("Europe/Moscow"))
                .toLocalDate();
        LocalDate startOfWeek = curDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = curDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        // convert LocalDate to Date
        Date start = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // get schedule for group on week
        List<Lesson> weekSchedule = l_repository.findLessonsOnWeek(group.getGroupName(), start, end);

        //get lessons with adding discipline
        List<Lesson> addingDisciplineSchedule = new ArrayList<>();
        for (Lesson l : weekSchedule){
            if (l.getDiscipline().equals(discipline)){
                addingDisciplineSchedule.add(l);
            }
        }

        if (countHoursAtWeek > addingDisciplineSchedule.size()){
            retVal += "\nNeed to add more classes "
                    + discipline.getDisciplineName()
                    + " at this week";
        } else if (countHoursAtWeek < addingDisciplineSchedule.size()){
            retVal += "\nProbably, you add too much classes "
                    + discipline.getDisciplineName()
                    + " at this week";
        }

        return retVal;
    }

    public static Duration getPositiveDuration(Instant start, Instant end) {
        return Duration.between(
                start.compareTo(end) <= 0 ? start : end,
                start.compareTo(end) <= 0 ? end : start
        );
    }

    Lesson getPrevious(List<Lesson> lessons, Date timeCur){
        if (lessons.isEmpty()){
            return null;
        }
        Instant curDateInstant = timeCur.toInstant();
        Duration minDur = getPositiveDuration(curDateInstant, lessons.get(0).getId().getDate().toInstant());
        boolean isFirstClass = !(timeCur.after(lessons.get(0).getId().getDate()));
        Lesson ret = lessons.get(0);
        for (int i = 1; i< lessons.size(); ++i){
            Date lTime = lessons.get(i).getId().getDate();
            Duration newDur = getPositiveDuration(curDateInstant, lTime.toInstant());
            minDur = (minDur.compareTo(newDur) < 0) ? newDur : minDur;
            ret = (minDur.compareTo(newDur) < 0) ? lessons.get(i) : ret;
            if (timeCur.after(lTime)){
                isFirstClass = false;
            }
        }

        return ret;
    }

    boolean isNormalLessonCheckerAnswer(String answer){
        boolean ret = false;
        List<String> templates = new ArrayList<>();
        templates.add("\nNeed to add more classes");
        templates.add("\nProbably, you add too much classes");

        if (answer.startsWith(templates.get(0))){
            ret = true;
        } else if (answer.startsWith(templates.get(1))) {
            ret = true;
        } else if (answer.isEmpty()){
            ret = true;
        }

        return ret;
    }
}
