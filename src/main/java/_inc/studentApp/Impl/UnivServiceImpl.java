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

        return s_repository.save(student);
    }
    @Override
    public Student findStudentByEmail(String email) {
        return s_repository.findByEmail(email);
    }
    @Override
    public String updateStudent(Student student) {
        Student old = s_repository.findByEmail(student.getEmail());
        if (old == null){
            return "There is no student " + student.getEmail();
        }

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

        DisciplinesKey key = new DisciplinesKey(request.getDisciplineName(), group.getGroupName(), teacher.getEmail());
        Disciplines discipline = new Disciplines(key, request.getCountHours(), group, teacher,null);

        return d_repository.save(discipline);
    }
    @Override
    public Optional<Disciplines> findByDisciplineKey(DisciplinesKey dk){ return d_repository.findById(dk); }
    @Override
    public String updateDiscipline(DisciplineUpdRequest request) {
        DisciplinesKey oldKey = new DisciplinesKey(
                request.getDisciplineName(),
                request.getGroupName(),
                request.getTeacherEmail()
        );
        Optional<Disciplines> old = d_repository.findById(oldKey);
        if (old.isEmpty()){
            return "There is no discipline " + request.getDisciplineName();
        }
        Optional<Group> newGroup = g_repository.findByGroupName(request.getGroupName());
        Optional<Employee> newTeacher = e_repository.findByEmail(request.getNewTeacherEmail());
        if (newTeacher.isEmpty() || newGroup.isEmpty()){
            String log = "";
            if (newTeacher.isEmpty()){
                log += "Can not update. Teacher " + request.getNewTeacherEmail() + " not exist ";
            }
            if (newGroup.isEmpty()){
                log += "Can not update. Group " + request.getNewGroupName() + " not exist";
            }
        }
        Disciplines newDis = d_repository.update(
                request.getDisciplineName(),
                request.getGroupName(),
                request.getTeacherEmail(),
                request.getNewDisciplineName(),
                request.getNewGroupName(),
                request.getNewTeacherEmail(),
                request.getNewCountHours()
        );

        return "Successfully update discipline " + newDis.getDisciplineName();
    }
    @Override
    @Transactional
    public void deleteDiscipline(DisciplinesKey key) {
        d_repository.deleteById(key);
    }

    // methods for units
    @Override
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
    public String updateUnit(UnitUpdRequest request) {
        Optional<Unit> old = un_repository.findById(request.getOldName());
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
    public String updateClassRoom(ClassRoomUpdRequest request) {
        /*ClassRoom newClassRoom = new ClassRoom();*/
        /*newClassRoom.setId(key);
        newClassRoom.setCapacity(request.getNewCapacity());
        newClassRoom.setUnit(unit);
        c_repository.deleteById(request.getOld());
        c_repository.save(newClassRoom);*/

        ClassRoomKey key = new ClassRoomKey(request.getOld().getClassroomNumber(), request.getOld().getUnitName());
        Optional<ClassRoom> old = c_repository.findById(key);
        if (old.isEmpty()){
            return "There is no classroom " + key.getClassroomNumber() + " in unit " + key.getUnitName();
        }

        c_repository.update(
                key.getUnitName(),
                request.getOld().getClassroomNumber(),
                request.getNewNumber(),
                request.getNewCapacity()
        );
        return "Classroom " + request.getNewNumber() + " successfully update";
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

        String isNormal = isNormalLesson(discipline, classRoom, request);

        if (isNormalLessonCheckerAnswer(isNormal)) {
            l_repository.save(request.toEntity(discipline, classRoom));
        } else{
            isNormal = "Lesson not added." + isNormal;
        }

        return isNormal;
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
    public String updateLesson(LessonUpdRequest request) {
        Disciplines discipline = d_repository
                .findById(
                        new DisciplinesKey(request.getNewDisciplineName(), request.getNewGroupName(), request.getNewTeacherEmail())
                ).orElseThrow();
        ClassRoom classRoom = c_repository
                .findById(
                        new ClassRoomKey(request.getNewClassroomNumber(), request.getNewUnitName())
                ).orElseThrow();

        Lesson newLesson = new Lesson(request.getNewId(), classRoom, discipline);

        String isNormal = isNormalLesson(
                discipline,
                classRoom,
                new LessonRequest(
                        request.getNewDisciplineName(),
                        request.getNewGroupName(),
                        request.getNewTeacherEmail(),
                        request.getNewClassroomNumber(),
                        request.getNewUnitName(),
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
        Group group = discipline.getGroupName();
        int countStudents = group.getStudents().size();
        if (countStudents > classRoom.getCapacity()){
            retVal += "\nClassroom "
                    + classRoom.getId().getClassroomNumber()
                    + " has insufficient capacity for group "
                    + group.getGroupName();
        }

        // is groups intersection
        List<Lesson> lessonsAtDate = l_repository.findLessonsByDate(request.getDate());
        int countStudentsGlobal = countStudents;
        int countGroups = 1;
        for (Lesson l : lessonsAtDate){
            if (l.getId().getDate().compareTo(request.getDate()) == 0
                    && l.getId().getClassroom().getClassroomNumber().equals(classRoom.getId().getClassroomNumber())
                    && !( (group.getGroupName()) .equals(l.getDiscipline().getId().getGroupName()) ) ){
                countStudentsGlobal += l.getDiscipline().getGroupName().getStudents().size();
                ++ countGroups;
            }
        }
        if (countStudentsGlobal != countStudents){
            retVal += "\n Classroom "
                    + classRoom.getId().getClassroomNumber()
                    + " has insufficient capacity for "
                    + countGroups + " groups ";
        }

        // is normal time
        for (Lesson l : lessonsAtDate){
            if ( (l.getDiscipline().getId().getGroupName()) .equals(group.getGroupName())
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
            if ( (l.getDiscipline().getId().getGroupName()) .equals(group.getGroupName()) ){
                lessonAtDateAddingGroup.add(l);
            }
        }
        // find previous lesson
        Lesson prevLesson = getPrevious(lessonAtDateAddingGroup, request.getDate());
        // check transfer time
        if (prevLesson != null && !(prevLesson.getId().getClassroom().getUnitName().equals(classRoom.getId().getUnitName()))){
            Duration timeToPrevLesson = getPositiveDuration(
                    request.getDate().toInstant(),
                    prevLesson.getId().getDate().toInstant()
                    );

            long minutesToPrevious = timeToPrevLesson.toMinutes() - 90; // -90 for get end of lesson
            Distance distance = dist_repository.findById(
                    new DistanceKey(
                            request.getUnitName(),
                            prevLesson.getClassroom().getUnit().getUnitName()
                    )
            ).orElseThrow();
            long timeBetweenUnits = distance.getTimeMinutes();

            if (timeBetweenUnits > minutesToPrevious){
                retVal += "\nTime between units "
                        + request.getUnitName()
                        + " and "
                        + prevLesson.getClassroom().getUnit().getUnitName()
                        + " is too long. Choose other unit for this class.";
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
