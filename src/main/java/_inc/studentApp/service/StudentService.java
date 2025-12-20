package _inc.studentApp.service;


import _inc.studentApp.DTO.DisciplineAndTeacherRequest;
import _inc.studentApp.DTO.DisciplineDTO;
import _inc.studentApp.DTO.EmployeeDTO;
import _inc.studentApp.DTO.TeacherForStudentDTO;
import _inc.studentApp.model.*;
import _inc.studentApp.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("myStudentService")
@AllArgsConstructor
public class StudentService {

    private LessonRepository l_repository;
    private GroupRepository g_repository;
    private DisciplineRepository d_repository;
    private StudentRepository s_repository;
    private UserRepository u_repository;

    public List<Lesson> findLessonsOnWeek(String groupName){

        Optional<Group> group = g_repository.findByGroupName(groupName);
        if (group.isEmpty()){
            throw new RuntimeException("Group " + groupName + " not exist.");
        }

        // получаем дни начала и конца недели
        LocalDate curDate = LocalDate.now();
        LocalDate startOfWeek = curDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = curDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // конвертируем из LocalDate в Date
        Date start = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return l_repository.findLessonsOnWeek(groupName, start, end);
    }

    public List<Lesson> findAllLessons(String groupName){
        return l_repository.findAllWithGroup(groupName);
    }

    public List<DisciplineAndTeacherRequest> getDisciplinesAndTeachers(String groupName){
        List<Disciplines> disciplines = d_repository.findByGroupName(groupName);
        List<DisciplineAndTeacherRequest> requests = new ArrayList<>();
        for (Disciplines d : disciplines){
            DisciplineAndTeacherRequest newRequest = new DisciplineAndTeacherRequest();
            newRequest.setDiscipline(DisciplineDTO.fromEntity(d));

            Employee teacher = d.getTeacher();
            List<Position> positions;
            if (teacher != null){
                positions = teacher.getPositions();
                newRequest.setTeacher(TeacherForStudentDTO.fromEntity(teacher));
                newRequest.setPositions(
                                positions.stream()
                                .map(Position::getPositionName)
                                .collect(Collectors.toList())
                );
            }

            requests.add(newRequest);
        }
        return requests;
    }

    public Student getInfo(){
        String username = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            username = authentication.getName();
        } else {
            throw new RuntimeException("Need authentification.");
        }

        Optional<User> user = u_repository.findByUserName(username);
        if (user.isPresent()){
            String email = user.get().getEmail();
            return s_repository.findByEmail(email);
        }

        return null;
    }
}
