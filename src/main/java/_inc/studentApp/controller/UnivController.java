package _inc.studentApp.controller;

import _inc.studentApp.DTO.*;
import _inc.studentApp.complexKeys.ClassRoomKey;
import _inc.studentApp.complexKeys.DisciplinesKey;
import _inc.studentApp.complexKeys.DistanceKey;
import _inc.studentApp.complexKeys.LessonKey;
import _inc.studentApp.model.*;
import _inc.studentApp.service.MyUserDetailsService;
import _inc.studentApp.service.UnivService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Admin methods")
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class UnivController {

    // service
    private final UnivService service;

    // security
    @Operation(
            summary = "Получить список всех пользователей",
            description = "Возвращает полный список пользователей как массив json User. Требует роль ADMIN.",
            tags = {"Users"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список пользователей успешно получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get_users")
    public List<User> findAllUsers(){
        return service.findAllUsers();
    }
    @PostMapping("/new-user")
    @Operation(
            summary = "Регистрация пользователя",
            description = "Создает нового пользователя или обновляет существующего. Не требуется роль.",
            tags = {"Users"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные пользователя для сохранения",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                  "userName" : "math teacher",
                                                  "password":"22",
                                                  "email": "Mac63@yahoo.com",
                                                  "role":"ROLE_TEACHER"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно сохранен или не сохранён по причине непрохождения проверок данных",
                            content =@Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            type = "string",
                                            example = """
                                                    ---------Вариант 1---------
                                                    User math teacher successfully added
                                                    ---------Вариант 2---------
                                                    User math teacher not added. <причины>
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Введенные данные некорректны.",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public String addUser(@RequestBody User user){
        return service.saveUser(user);
    }
    @GetMapping("/user-{uname}")
    @PreAuthorize("hasRole('ADMIN')")
    public User findUserByUserName(@PathVariable("uname") String username){
        return service.findUserByUserName(username);
    }
    @PutMapping("/update-user")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUser(@RequestBody User user){
        return service.updateUser(user);
    }
    @DeleteMapping("/delete-user-{uname}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteByUserName(@PathVariable("uname") String username){
        service.deleteUser(username);
    }
    @DeleteMapping("/deleteall-users")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllUsers(){
        service.deleteAllUsers();
    }

    // student methods
    @Operation(
            summary = "Получить список всех студентов",
            description = "Возвращает полный список студентов как массив json StudentDTO. Требует роль ADMIN.",
            tags = {"Students"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список студентов успешно получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = StudentDTO.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/get_students")
    @PreAuthorize("hasRole('ADMIN')")
    public List<StudentDTO> findAllStudent(){
        return service.findAllStudent().stream().map(StudentDTO::fromEntity).collect(Collectors.toList());
    }

    @PostMapping("/save_student")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Добавить студента",
            description = "Создает нового студента или обновляет существующего. Требует роль ADMIN.",
            tags = {"Students"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные студента для сохранения",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Student.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                 "email": "test@gmail.com",
                                                 "firstName": "fName",
                                                 "lastName": "sName",
                                                 "dateOfBirth": "2006-03-18",
                                                 "group":{"groupName":"23.Б11-ПУ"},
                                                 "level": "bac",
                                                 "enterYear": "2023"
                                             }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Студент успешно сохранен",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            type = "string",
                                            example = "Student test@gmail.com successfully saved"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN) или введенные данные некорректны.",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public String saveStudent(@RequestBody Student student) {
        service.saveStudent(student);
        return "Student " + student.getEmail() + " successfully saved";
    }
    @GetMapping("/student-{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public StudentDTO findStudentByEmail(@PathVariable("email") String email) {
        return StudentDTO.fromEntity(service.findStudentByEmail(email));
    }
    @PutMapping("/update_student")
    @PreAuthorize("hasRole('ADMIN')")
    public StudentDTO updateStudent(@RequestBody Student student) {
        return StudentDTO.fromEntity(service.updateStudent(student));
    }

    @DeleteMapping("/delete_student/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteStudent(@PathVariable String email) {
        service.deleteStudent(email);
    }


    // employee methods
    @Operation(
            summary = "Получить список всех сотрудников",
            description = "Возвращает полный список сотрудников как массив json EmployeeDTO. Требует роль ADMIN.",
            tags = {"Employees"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список сотрудников успешно получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = EmployeeDTO.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/get_employees")
    @PreAuthorize("hasRole('ADMIN')")
    public List<EmployeeDTO> findAllEmployee(){
        return service.findAllEmployee().stream().map(EmployeeDTO::fromEntity).collect(Collectors.toList());
    }
    @PostMapping("/save_employee")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Добавить сотрудника",
            description = "Создает нового сотрудника или обновляет существующего. Требует роль ADMIN.",
            tags = {"Employees"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные сотрудника для сохранения",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                  "firstName":"fName",
                                                  "lastName":"sName",
                                                  "dateOfBirth":"1970-01-01",
                                                  "email":"teacherTest@gmail.com",
                                                  "experience":30,
                                                  "positions": ["teacher"]
                                              }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Сотрудник успешно сохранен",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            type = "string",
                                            example = "Employee teacherTest@gmail.com successfully saved"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN) или введенные данные некорректны.",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public String saveEmployee(@RequestBody EmployeeRequest request) {
        Employee employee = service.saveEmployee(request);
        return "Employee " + employee.getEmail() + " successfully saved";
    }
    @GetMapping("/employee-{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public Optional<Employee> findEmployeeByEmail(@PathVariable("email") String email) {
        return service.findEmployeeByEmail(email);
    }
    @PutMapping("/update_employee")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateEmployee(@RequestBody EmployeeRequest request) {
        return service.updateEmployee(request);
    }
    @DeleteMapping("/delete_employee/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEmployee(@PathVariable String email) {
        service.deleteEmployee(email);
    }

    // group methods
    @Operation(
            summary = "Получить список всех учебных групп",
            description = "Возвращает полный список учебных групп как массив json Group. Требует роль ADMIN.",
            tags = {"Groups"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список учебных групп успешно получен",
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
                            description = "Недостаточно прав (требуется роль ADMIN)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/get_groups")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Group> findAllGroup(){
        return service.findAllGroups();
    }
    @PostMapping("/save_group")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Добавить учебную группу",
            description = "Создает новую учебную группу или обновляет существующую. Требует роль ADMIN.",
            tags = {"Groups"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные группы для сохранения",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Group.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "groupName" : "23.Б11-ПУ"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Группа успешно сохранена",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            type = "string",
                                            example = "Group 23.Б11-ПУ successfully saved"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN) или введенные данные некорректны.",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public String saveGroup(@RequestBody Group group) {
        service.saveGroup(group);
        return "Group " + group.getGroupName() + " successfully saved";
    }
    @GetMapping("/group-{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public Optional<Group> findGroupByName(@PathVariable("name") String name) {
        return service.findByGroupName(name);
    }
    @PutMapping("/update_group/{name}/{newName}")
    @PreAuthorize("hasRole('ADMIN')")
    public Group updateGroup(@PathVariable("name") String name, @PathVariable("newName") String newName) {
        return service.updateGroup(name, newName);
    }
    @DeleteMapping("/delete_group/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteGroup(@PathVariable("name") String name) {
        service.deleteGroup(name);
    }


    // position methods
    @Operation(
            summary = "Получить список всех должностей",
            description = "Возвращает полный список должностей как массив json Position. Требует роль ADMIN.",
            tags = {"Positions"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список должностей успешно получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Position.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/get_positions")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Position> findAllPosition(){
        return service.findAllPositions();
    }
    @PostMapping("/save_position")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Добавить должность",
            description = "Создает новую должность или обновляет существующую. Требует роль ADMIN.",
            tags = {"Positions"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные должности для сохранения",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Position.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "positionName" : "admin"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Должность успешно сохранена",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            type = "string",
                                            example = "Position admin successfully saved"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN) или введенные данные некорректны.",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public String savePosition(@RequestBody Position position) {
        service.savePosition(position);
        return "Position " + position.getPositionName() + " successfully saved";
    }
    @GetMapping("/position-{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public Optional<Position> findPositionByName(@PathVariable("name") String name) {
        return service.findByPositionName(name);
    }
    @PutMapping("/update_position")
    @PreAuthorize("hasRole('ADMIN')")
    public Position updatePosition(@RequestBody Position position) {
        return service.updatePosition(position);
    }
    @DeleteMapping("/delete_position/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePosition(@PathVariable Long id) {
        service.deletePosition(id);
    }


    // discipline methods
    @Operation(
            summary = "Получить список всех преподаваемых дисциплин",
            description = "Возвращает полный список преподаваемых дисциплин как массив json DisciplineDTO. Требует роль ADMIN.",
            tags = {"Disciplines"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список преподаваемых дисциплин успешно получен",
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
                            description = "Недостаточно прав (требуется роль ADMIN)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/get_disciplines")
    @PreAuthorize("hasRole('ADMIN')")
    public List<DisciplineDTO> findAllDisciplines(){
        return service.findAllDisciplines().stream().map(DisciplineDTO::fromEntity).collect(Collectors.toList());
    }
    @PostMapping("/save_discipline")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Добавить учебную дисциплину",
            description = "Создает новую учебную дисциплину или обновляет существующую. Требует роль ADMIN.",
            tags = {"Disciplines"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные учебнй дисциплины для сохранения",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DisciplineRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                 "disciplineName" : "math",
                                                 "groupName":"23.Б11-ПУ",
                                                 "teacherEmail":"Linda_Blanda@yahoo.com",
                                                 "countHours":"100"
                                             }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Дисциплина успешно сохранена",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            type = "string",
                                            example = "Discipline math successfully saved"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN) или введенные данные некорректны.",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public String saveDiscipline(@RequestBody DisciplineRequest request) {
        Disciplines created = service.saveDiscipline(request);
        return "Discipline " + created.getDisciplineName() + " successfully saved";
    }
    @GetMapping("/discipline_find")
    @PreAuthorize("hasRole('ADMIN')")
    public DisciplineDTO findDisciplineByName(@RequestBody DisciplinesKey dk) {
        Disciplines discipline = service.findByDisciplineKey(dk).orElseThrow(() -> new EntityNotFoundException("Discipline not found"));
        return DisciplineDTO.fromEntity(discipline);
    }
    @PutMapping("/update_discipline")
    @PreAuthorize("hasRole('ADMIN')")
    public DisciplineDTO updateDiscipline(@RequestBody DisciplineUpdRequest request) {
        Disciplines disciplineUpd = service.updateDiscipline(request);
        return DisciplineDTO.fromEntity(disciplineUpd);
    }
    @DeleteMapping("/delete_discipline")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDiscipline(@RequestBody DisciplineRequest request) {
        service.deleteDiscipline(request);
    }

    // units methods
    @Operation(
            summary = "Получить список всех подразделений ВУЗа",
            description = "Возвращает полный список подразделений ВУЗа как массив json Unit. Требует роль ADMIN.",
            tags = {"Units"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список подразделений ВУЗа успешно получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Unit.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/get_units")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Unit> findAllUnits(){
        return service.findAllUnits();
    }
    @PostMapping("/save_unit")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Добавить подразделение ВУЗа",
            description = "Создает новое подразделение ВУЗа или обновляет существующее. Требует роль ADMIN.",
            tags = {"Units"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные подразделения ВУЗа для сохранения",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Unit.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                  "unitName" : "AM-CP faculty",
                                                  "address": "Университетский пр., 35"
                                              }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Подразделение успешно сохранено",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            type = "string",
                                            example = "Unit AM-CP faculty at address Университетский пр., 35 successfully saved"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN) или введенные данные некорректны.",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public String saveUnit(@RequestBody Unit unit) {
        service.saveUnit(unit);
        return "Unit " + unit.getUnitName() + " at address " + unit.getAddress() + " successfully saved";
    }
    @GetMapping("/unit-{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public Optional<Unit> findUnitByName(@PathVariable("name") String name) {
        return service.findUnitByName(name);
    }
    @PutMapping("/update_unit")
    @PreAuthorize("hasRole('ADMIN')")
    public Unit updateUnit(@RequestBody UnitUpdRequest request) {
        return service.updateUnit(request);
    }
    @DeleteMapping("/delete_unit/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUnit(@PathVariable String name) {
        service.deleteUnit(name);
    }

    // classroom methods
    @Operation(
            summary = "Получить список всех аудиторий.",
            description = "Возвращает полный список аудиторий как массив json ClassRoomRequest. Требует роль ADMIN.",
            tags = {"ClassRooms"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список аудиторий успешно получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ClassRoomRequest.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/get_classrooms")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ClassRoomRequest> findAllClassRooms(){
        return service.findAllClassRooms().stream().map(ClassRoomRequest::fromEntity).collect(Collectors.toList());
    }
    @PostMapping("/save_classroom")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Добавить аудиторию",
            description = "Создает новую аудиторию или обновляет существующую. Требует роль ADMIN.",
            tags = {"ClassRooms"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные аудитории для сохранения",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClassRoomRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                   "classroomNumber" : "210Д",
                                                   "unitName" : "AM-CP faculty",
                                                   "capacity" : 100
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Аудитория успешно сохранена",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            type = "string",
                                            example = "Classroom 210Д in unit AM-CP faculty successfully saved"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN) или введенные данные некорректны.",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public String saveClassRoom(@RequestBody ClassRoomRequest request) {
        ClassRoom created = service.saveClassRoom(request);
        return "ClassRoom " + created.getId().getClassroomNumber() + " in unit " + created.getId().getUnitName() + " successfully saved";
    }
    @GetMapping("/classroom_find")
    @PreAuthorize("hasRole('ADMIN')")
    public ClassRoomRequest findClassRoomById(@RequestBody ClassRoomKey key) {
        ClassRoom classRoom = service.findClassRoomByID(key).orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
        return ClassRoomRequest.fromEntity(classRoom);
    }
    @PutMapping("/update_classroom")
    @PreAuthorize("hasRole('ADMIN')")
    public ClassRoomRequest updateClassRoom(@RequestBody ClassRoomUpdRequest request) {
        ClassRoom classRoomUpd = service.updateClassRoom(request);
        return ClassRoomRequest.fromEntity(classRoomUpd);
    }
    @DeleteMapping("/delete_classroom")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteClassRoom(@RequestBody ClassRoomKey key) {
        service.deleteClassRoom(key);
    }

    // distance methods
    @Operation(
            summary = "Получить список всех расстояний между подразделениями ВУЗа.",
            description = "Возвращает полный список расстояний между подразделениями ВУЗа (в минутах) как массив json DistanceRequest. Требует роль ADMIN.",
            tags = {"Distances"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список дистанций успешно получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = DistanceRequest.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/get_distances")
    @PreAuthorize("hasRole('ADMIN')")
    public List<DistanceRequest> findAllDistances(){
        return service.findAllDistances().stream().map(DistanceRequest::fromEntity).collect(Collectors.toList());
    }
    @PostMapping("/save_distance")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Добавить дистанцию между подразделениями",
            description = "Создает новую дистанцию (в минутах) или обновляет существующую. Требует роль ADMIN.",
            tags = {"Distances"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные дистанции для сохранения",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DistanceRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                 "unitFrom" : "AM-CP faculty",
                                                 "unitTo": "Central unit",
                                                 "time" : 90
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Дистанция успешно сохранена",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            type = "string",
                                            example = "Distance from AM-CP faculty to Central unit successfully saved"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN) или введенные данные некорректны.",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public String saveDistance(@RequestBody DistanceRequest request) {
        Distance created = service.saveDistance(request);
        return "Distance from " + created.getId().getUnitFrom() + " to " + created.getId().getUnitTo() + " successfully saved";
    }
    @GetMapping("/distance_find")
    @PreAuthorize("hasRole('ADMIN')")
    public DistanceRequest findDistanceById(@RequestBody DistanceKey key) {
        Distance dist = service.findDistanceByID(key).orElseThrow(() -> new EntityNotFoundException("Distance not found"));
        return DistanceRequest.fromEntity(dist);
    }
    @PutMapping("/update_distance")
    @PreAuthorize("hasRole('ADMIN')")
    public DistanceRequest updateDistance(@RequestBody DistanceRequest request) {
        Distance distUpd = service.updateDistance(request);
        return DistanceRequest.fromEntity(distUpd);
    }
    @DeleteMapping("/delete_distance")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDistance(@RequestBody DistanceKey key) {
        service.deleteDistance(key);
    }

    // lesson methods
    @Operation(
            summary = "Получить расписание за весь период сохраненный в БД.",
            description = "Возвращает полный список всех занятий, которые сохранены в БД как массив json LessonRequest. Требует роль ADMIN.",
            tags = {"Schedule"},
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
                            description = "Недостаточно прав (требуется роль ADMIN)",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/get_lessons")
    @PreAuthorize("hasRole('ADMIN')")
    public List<LessonRequest> findAllLessons(){
        return service.findAllLessons().stream().map(LessonRequest::fromEntity).collect(Collectors.toList());
    }
    @PostMapping("/save_lesson")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Добавить занятие",
            description = "Создает новое занятие или обновляет существующее. Требует роль ADMIN.",
            tags = {"Schedule"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные занятия для сохранения",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LessonRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                 "disciplineName": "programming",
                                                 "groupName": "23.Б11-ПУ",
                                                 "teacherEmail": "Karen_Pagac75@yahoo.com",
                                                 "classroomNumber": "test aud",
                                                 "unitName": "Central unit",
                                                 "date" : "2025-11-04T17:30:00.000+03:00"
                                             }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Занятие успешно сохранено или не добавлено т.к. не прошло проверки",
                            content = @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(
                                            type = "string",
                                            example = """
                                                    ---------Вариант 1---------
                                                    Lesson of programming for group 23.Б11-ПУ with teacher Karen_Pagac75@yahoo.com in classroom test aud successfully saved! <опционально: данные о том больше или меньше занятий, относительно учебного плана добавлено>
                                                    ---------Вариант 2---------
                                                    Lesson not added. <список причин>
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN) или введенные данные некорректны.",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public String saveLesson(@RequestBody LessonRequest request) {
        String createLog = service.saveLesson(request);
        if (!createLog.startsWith("Lesson not added.")) {
            return "Lesson of " + request.getDisciplineName()
                    + " for group " + request.getGroupName()
                    + " with teacher " + request.getTeacherEmail()
                    + " in classroom " + request.getClassroomNumber()
                    + " successfully saved!\n"
                    + createLog;
        }
        return createLog;
    }
    @GetMapping("/lesson_find")
    @PreAuthorize("hasRole('ADMIN')")
    public LessonRequest findLessonById(@RequestBody LessonRequest request) {
        Lesson lesson = service.findLessonByID(request).orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
        return LessonRequest.fromEntity(lesson);
    }
    @GetMapping("/find_lesson_ondate")
    @PreAuthorize("hasRole('ADMIN')")
    public List<LessonRequest> findLessonsByDate(@RequestBody DateRequest request){
        return service.findLessonsByDate(request.getDate()).stream().map(LessonRequest::fromEntity).collect(Collectors.toList());
    }
    @PutMapping("/update_lesson")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateLesson(@RequestBody LessonUpdRequest request) {
        String createLog = service.updateLesson(request);
        if (!createLog.startsWith("Cannot update lesson with causes:")) {
            return "Lesson of " + request.getNewDisciplineName()
                    + " for group " + request.getNewGroupName()
                    + " with teacher " + request.getNewTeacherEmail()
                    + " in classroom " + request.getNewClassroomNumber()
                    + " successfully updated!\n"
                    + createLog;
        }
        return createLog;
    }
    @DeleteMapping("/delete_lesson")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteLesson(@RequestBody LessonRequest request) {
        service.deleteLesson(request);
    }
    @DeleteMapping("/delete_all_lessons")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllLessons() {
        service.deleteAllLessons();
    }

}
