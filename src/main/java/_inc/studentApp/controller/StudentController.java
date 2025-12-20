package _inc.studentApp.controller;

import _inc.studentApp.DTO.DisciplineAndTeacherRequest;
import _inc.studentApp.DTO.LessonRequest;
import _inc.studentApp.DTO.StudentDTO;
import _inc.studentApp.service.StudentService;
import _inc.studentApp.model.Student;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Student methods")
@RestController
@RequestMapping("/api/v1/student")
@AllArgsConstructor
public class StudentController {

    private StudentService service;

    @GetMapping("/schedule-week-{groupName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @Operation(
            summary = "Получить расписание на неделю",
            description = "Возвращает список занятий на текущую неделю для группы из пути как массив json LessonRequest. Требует роль ADMIN или STUDENT.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список занятий успешно получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = LessonRequest.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN или STUDENT) или указанной группы не существует",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public List<LessonRequest> findLessonsOnWeek(@PathVariable("groupName") String groupName){
        return service.findLessonsOnWeek(groupName)
                .stream().map(LessonRequest::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/schedule-full-{groupName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @Operation(
            summary = "Получить расписание за весь период сохраненный в бд",
            description = "Возвращает список занятий за весь период для группы из пути как массив json LessonRequest. Требует роль ADMIN или STUDENT.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список занятий успешно получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = LessonRequest.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN или STUDENT)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public List<LessonRequest> findAllLessons(@PathVariable("groupName") String groupName){
        return service.findAllLessons(groupName)
                .stream().map(LessonRequest::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/disciplines-and-teachers-{groupName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @Operation(
            summary = "Получить писок дисциплин и преподавателей",
            description = "Возвращает список дисциплин и преподавателей, которые их ведут, для группы из пути как массив json DisciplineAndTeacherRequest. Требует роль ADMIN или STUDENT.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список занятий успешно получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DisciplineAndTeacherRequest.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN или STUDENT)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public List<DisciplineAndTeacherRequest> getDisciplinesAndTeachers(@PathVariable("groupName") String groupName){
        return service.getDisciplinesAndTeachers(groupName);
    }

    @GetMapping("/about-me")
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(
            summary = "Получить информацию о себе",
            description = "Возвращает json Student с информацией студента, сохранённой в БД. При вызове смотрит какой пользоваетль на данный момент авторизован и если это студент - выдаёт информацию о нём. Требует роль STUDENT.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация успешно получена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Student.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль STUDENT)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public Student getInfo(){
        return service.getInfo();
    }
}
