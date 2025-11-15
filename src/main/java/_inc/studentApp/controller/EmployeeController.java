package _inc.studentApp.controller;

import _inc.studentApp.DTO.DisciplineAndTeacherRequest;
import _inc.studentApp.DTO.DisciplineDTO;
import _inc.studentApp.DTO.EmployeeDTO;
import _inc.studentApp.DTO.LessonRequest;
import _inc.studentApp.model.Employee;
import _inc.studentApp.model.Group;
import _inc.studentApp.model.Student;
import _inc.studentApp.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(
            summary = "Получить расписание на неделю",
            description = "Возвращает список занятий на текущую неделю для преподавателя из пути как массив json LessonRequest. Требует роль ADMIN или TEACHER.",
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
                            description = "Недостаточно прав (требуется роль ADMIN или TEACHER)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public List<LessonRequest> findLessonsOnWeek(@PathVariable("email") String email){
        return service.findLessonsOnWeek(email)
                .stream().map(LessonRequest::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/schedule-full-{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
            summary = "Получить расписание за весь период сохраненный в бд",
            description = "Возвращает список занятий за весь период для преподавателя из пути как массив json LessonRequest. Требует роль ADMIN или TEACHER.",
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
                            description = "Недостаточно прав (требуется роль ADMIN или TEACHER)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public List<LessonRequest> findAllLessons(@PathVariable("email") String email){
        return service.findAllLessons(email)
                .stream().map(LessonRequest::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/groups")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(
            summary = "Получить список групп для преподавателя",
            description = "Возвращает список групп, у которых ведет преподаватель, отправляющий запрос как массив json Group. Требует роль TEACHER.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список групп успешно получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Group.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль TEACHER)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public List<Group> getGroups(){
        return service.getGroupsWithTeacher();
    }

    @GetMapping("/students-{group}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
            summary = "Получить список студентов из группы",
            description = "Возвращает список студентов, обучающихся в группе из пути, как массив json Student. Проверяется ведет ли отправляющий запрос преподаватель в указанной группе. Требует роль TEACHER.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список студентов успешно получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Student.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль TEACHER)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public List<Student> getStudentByGroup(@PathVariable("group") String groupName){
        return service.getStudentByGroup(groupName);
    }

    @GetMapping("/about-me")
    @PreAuthorize("hasRole('TEACHER')")
    @Operation(
            summary = "Получить информацию о себе",
            description = "Возвращает json EmployeeDTO с информацией сотрудника, сохранённой в БД. При вызове смотрит какой пользоваетль на данный момент авторизован и если это преподаватель - выдаёт информацию о нём. Требует роль TEACHER.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация успешно получена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EmployeeDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль TEACHER)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public EmployeeDTO getInfo(){
        return EmployeeDTO.fromEntity(service.getInfo());
    }

    @GetMapping("/disciplines-and-groups-{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(
            summary = "Получить список дисциплин и групп",
            description = "Возвращает список дисциплин и групп, которые ведет преподаватель из пути как массив json DisciplineDTO. Требует роль ADMIN или TEACHER.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список дисциплин успешно получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DisciplineDTO.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN или TEACHER)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public List<DisciplineDTO> getDisciplinesAndGroups(@PathVariable("email") String email){
        return service.getDisciplinesAndGroups(email)
                .stream().map(DisciplineDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
