package _inc.studentApp.controller;

import _inc.studentApp.DTO.DisciplineAndTeacherRequest;
import _inc.studentApp.DTO.DisciplineDTO;
import _inc.studentApp.DTO.EmployeeDTO;
import _inc.studentApp.DTO.LessonRequest;
import _inc.studentApp.model.Employee;
import _inc.studentApp.model.Group;
import _inc.studentApp.model.Student;
import _inc.studentApp.service.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Teachers methods")
@RestController
@RequestMapping("/api/v1/employee")
@AllArgsConstructor
public class EmployeeController {
    private EmployeeService service;

    @GetMapping("/schedule-week-{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public List<LessonRequest> findLessonsOnWeek(@PathVariable("email") String email){
        return service.findLessonsOnWeek(email)
                .stream().map(LessonRequest::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/schedule-full-{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public List<LessonRequest> findAllLessons(@PathVariable("email") String email){
        return service.findAllLessons(email)
                .stream().map(LessonRequest::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/groups")
    @PreAuthorize("hasRole('TEACHER')")
    public List<Group> getGroups(){
        return service.getGroupsWithTeacher();
    }

    @GetMapping("/students-{group}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public List<Student> getStudentByGroup(@PathVariable("group") String groupName){
        return service.getStudentByGroup(groupName);
    }

    @GetMapping("/about-me")
    @PreAuthorize("hasRole('TEACHER')")
    public EmployeeDTO getInfo(){
        return EmployeeDTO.fromEntity(service.getInfo());
    }

    @GetMapping("/disciplines-and-groups-{email}")
    public List<DisciplineDTO> getDisciplinesAndGroups(@PathVariable("email") String email){
        return service.getDisciplinesAndGroups(email)
                .stream().map(DisciplineDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
