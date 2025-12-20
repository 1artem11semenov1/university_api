package _inc.studentApp.service;

import _inc.studentApp.complexKeys.DisciplinesKey;
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
import java.util.*;

@Service("myEmployeeService")
@AllArgsConstructor
public class EmployeeService {

    private LessonRepository l_repository;
    private GroupRepository g_repository;
    private DisciplineRepository d_repository;
    private EmployeeRepository e_repository;
    private UserRepository u_repository;

    public List<Lesson> findLessonsOnWeek(String email){

        // получаем дни начала и конца недели
        LocalDate curDate = LocalDate.now();
        LocalDate startOfWeek = curDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = curDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // конвертируем из LocalDate в Date
        Date start = Date.from(startOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endOfWeek.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return l_repository.findTeacherLessonsOnWeek(email, start, end);
    }

    public List<Lesson> findAllLessons(String email){
        return l_repository.findAllWithTeacher(email);
    }

    public List<Group> getGroupsWithTeacher(){
        String username = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            username = authentication.getName();
        } else {
            throw new RuntimeException("Need authentification.");
        }

        Optional<User> user = u_repository.findByUserName(username);
        if (user.isPresent()){
            List<Disciplines> disciplines = d_repository.findByTeacherEmail(user.get().getEmail());
            Set<Group> groups = new HashSet<>();
            for (Disciplines d : disciplines){
                groups.add(g_repository.findByGroupName(d.getGroup().getGroupName()).orElseThrow());
            }

            return groups.stream().toList();
        }

        return null;
    }

    public List<Student> getStudentByGroup(String groupName){
        List<Group> groups = getGroupsWithTeacher();
        List<String> groupNames = groups.stream().map(Group::getGroupName).toList();
        if (groupNames.contains(groupName)){
            Group group = g_repository.findByGroupName(groupName).orElseThrow();
            return group.getStudents();
        }

        return null;
    }

    public Employee getInfo(){
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
            Optional<Employee> emp = e_repository.findByEmail(email);
            return emp.orElse(null);
        }

        return null;
    }

    public List<Disciplines> getDisciplinesAndGroups(String email){
        Optional<Employee> emp = e_repository.findByEmail(email);
        if (emp.isEmpty()){
            return null;
        }
        return d_repository.findByTeacherEmail(email);
    }
}
