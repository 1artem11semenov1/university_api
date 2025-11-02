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
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

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

    // TODO: неправильное вычисление промежутков, получаем -N минут, исправить
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
