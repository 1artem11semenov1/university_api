package _inc.studentApp.controller;

import _inc.studentApp.DTO.DisciplineAndTeacherRequest;
import _inc.studentApp.DTO.LessonRequest;
import _inc.studentApp.service.StudentService;
import _inc.studentApp.model.Student;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Student methods")
@RestController
@RequestMapping("/api/v1/student")
@AllArgsConstructor
public class StudentController {

    private StudentService service;

    @GetMapping("/schedule-week-{groupName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public List<LessonRequest> findLessonsOnWeek(@PathVariable("groupName") String groupName){
        return service.findLessonsOnWeek(groupName)
                .stream().map(LessonRequest::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/schedule-full-{groupName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public List<LessonRequest> findAllLessons(@PathVariable("groupName") String groupName){
        return service.findAllLessons(groupName)
                .stream().map(LessonRequest::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/disciplines-and-teachers-{groupName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public List<DisciplineAndTeacherRequest> getDisciplinesAndTeachers(@PathVariable("groupName") String groupName){
        return service.getDisciplinesAndTeachers(groupName);
    }

    @GetMapping("/about-me")
    @PreAuthorize("hasRole('STUDENT')")
    public Student getInfo(){
        return service.getInfo();
    }
}
