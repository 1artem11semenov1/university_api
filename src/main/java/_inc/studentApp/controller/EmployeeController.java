package _inc.studentApp.controller;

import _inc.studentApp.DTO.DisciplineAndTeacherRequest;
import _inc.studentApp.DTO.DisciplineDTO;
import _inc.studentApp.DTO.EmployeeDTO;
import _inc.studentApp.DTO.LessonRequest;
import _inc.studentApp.model.Employee;
import _inc.studentApp.model.Group;
import _inc.studentApp.model.Student;
import _inc.studentApp.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employee")
@AllArgsConstructor
public class EmployeeController {
    private EmployeeService service;

    @GetMapping("/schedule-week-{email}")
    public List<LessonRequest> findLessonsOnWeek(@PathVariable("email") String email){
        return service.findLessonsOnWeek(email)
                .stream().map(LessonRequest::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/schedule-full-{email}")
    public List<LessonRequest> findAllLessons(@PathVariable("email") String email){
        return service.findAllLessons(email)
                .stream().map(LessonRequest::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/groups")
    public List<Group> getGroups(){
        return service.getGroupsWithTeacher();
    }

    @GetMapping("/students-{group}")
    public List<Student> getStudentByGroup(@PathVariable("group") String groupName){
        return service.getStudentByGroup(groupName);
    }

    @GetMapping("/about-me")
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
