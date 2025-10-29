package _inc.studentApp.controller;

import _inc.studentApp.DTO.DisciplineAndTeacherRequest;
import _inc.studentApp.DTO.LessonRequest;
import _inc.studentApp.service.StudentService;
import _inc.studentApp.model.Student;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/student")
@AllArgsConstructor
public class StudentController {

    private StudentService service;

    @GetMapping("/schedule-week-{groupName}")
    public List<LessonRequest> findLessonsOnWeek(@PathVariable("groupName") String groupName){
        return service.findLessonsOnWeek(groupName)
                .stream().map(LessonRequest::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/schedule-full-{groupName}")
    public List<LessonRequest> findAllLessons(@PathVariable("groupName") String groupName){
        return service.findAllLessons(groupName)
                .stream().map(LessonRequest::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/disciplines-and-teachers-{groupName}")
    public List<DisciplineAndTeacherRequest> getDisciplinesAndTeachers(@PathVariable("groupName") String groupName){
        return service.getDisciplinesAndTeachers(groupName);
    }

    @GetMapping("/about-me")
    public Student getInfo(){
        return service.getInfo();
    }
}
