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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
            description = """
                    Создает нового пользователя или обновляет существующего. Не требуется роль.
                    Список проверок при добавлении:
                    - проверка на существование в таблицах со студентами и/или сотрудниками
                    - соответствие указанной роли должностям, сохраненным в БД.
                    """,
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
    @GetMapping("/user/{uname}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Найти пользователя по юзернейму",
            description = "Возвращает пользователя по его имени пользователя, которое берёт из пути /user/{uname}. Требует роль ADMIN.",
            tags = {"Users"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN) или пользователь не найден",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public User findUserByUserName(@PathVariable("uname") String username){
        return service.findUserByUserName(username);
    }
    @PutMapping("/update-user")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновить данные пользователя",
            description = "Обновляет уже добавленного ранее пользователя. Требует роль ADMIN. При обновлении осуществляются те же проверки, что при добавлении.",
            tags = {"Users"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные пользователя для обновления",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LessonUpdRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                  "userName" : "math teacher",
                                                  "password":"223",
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
                            description = "Пользователь успешно обновлен или новые параметры не прошли проверки",
                            content = @Content(
                                    schema = @Schema(
                                            type = "string",
                                            example = """
                                                    ---------Вариант 1---------
                                                    User <user_name> successfully added
                                                    ---------Вариант 2---------
                                                    Can not update <user_name> because user not exist.
                                                    ---------Вариант 3---------
                                                    User <user_name> not added. <причины>
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
    public String updateUser(@RequestBody User user){
        return service.updateUser(user);
    }
    @DeleteMapping("/delete-user/{uname}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Удаление пользователя по юзернейму",
            description = "Удаляет пользователя с указанным юзернеймом, который берется из пути. Требуется роль ADMIN",
            tags = {"Users"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление или пользователь не существовал"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)"
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void deleteByUserName(@PathVariable("uname") String username){
        service.deleteUser(username);
    }
    @DeleteMapping("/deleteall-users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Удаление всех пользователей",
            description = "Удаляет всех сохраненных пользователей. Требуется рель ADMIN",
            tags = {"Users"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)"
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
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
    @GetMapping("/student/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Найти студента по email",
            description = "Возвращает студента по его email, который берёт из пути /student/{email}. Требует роль ADMIN.",
            tags = {"Students"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Студент найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StudentDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN) или студент не найден",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public StudentDTO findStudentByEmail(@PathVariable("email") String email) {
        return StudentDTO.fromEntity(service.findStudentByEmail(email));
    }
    @PutMapping("/update_student")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновить данные студента",
            description = "Обновляет уже существующего студента. Требует роль ADMIN.",
            tags = {"Students"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные студента для обновления",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Student.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                 "email": "test@gmail.com",
                                                 "firstName": "newName",
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
                            description = "Студент успешно обновлён или такой студент не существует",
                            content = @Content(
                                    schema = @Schema(
                                            type = "string",
                                            example = """
                                                    ---------Вариант 1---------
                                                    Student test@gmail.com successfully updated.
                                                    ---------Вариант 2---------
                                                    There is no student test@gmail.com.
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
    public String updateStudent(@RequestBody Student student) {
        return service.updateStudent(student);
    }
    @DeleteMapping("/delete_student/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Удаление студента по email",
            description = "Удаляет студента с указанным email, который берется из пути. Требуется роль ADMIN",
            tags = {"Students"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление или студент не существовал"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)"
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
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
    @GetMapping("/employee/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Найти сотрудника по email",
            description = "Возвращает сотрудника по его email, который берёт из пути /employee/{email}. Требует роль ADMIN.",
            tags = {"Employees"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Сотрудник найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Employee.class)
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
    public Optional<Employee> findEmployeeByEmail(@PathVariable("email") String email) {
        return service.findEmployeeByEmail(email);
    }
    @PutMapping("/update_employee")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновить данные сотрудника",
            description = "Обновляет уже существующего сотрудника. Требует роль ADMIN.",
            tags = {"Employees"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные сотрудника для обновления",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                  "firstName":"newName",
                                                  "lastName":"sName",
                                                  "dateOfBirth":"1970-01-01",
                                                  "email":"teacherTest@gmail.com",
                                                  "experience":30,
                                                  "positions": ["teacher", "admin"]
                                              }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Сотрудник успешно обновлён или такой сотрудник не существует",
                            content = @Content(
                                    schema = @Schema(
                                            type = "string",
                                            example = """
                                                    ---------Вариант 1---------
                                                    Employee test@gmail.com successfully updated.
                                                    ---------Вариант 2---------
                                                    There is no employee test@gmail.com.
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
    public String updateEmployee(@RequestBody EmployeeRequest request) {
        return service.updateEmployee(request);
    }
    @DeleteMapping("/delete_employee/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Удаление сотрудника по email",
            description = "Удаляет сотрудника с указанным email, который берется из пути. Требуется роль ADMIN",
            tags = {"Employees"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление или сотрудник не существовал"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN) или есть ссылки в disciplines не этого сотрудника"
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
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
    @GetMapping("/group/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Найти учебную группу по названию",
            description = "Возвращает учебную группу по её названию, которое берёт из пути /group/{name}. Требует роль ADMIN.",
            tags = {"Groups"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Группа найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Group.class)
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
    public Optional<Group> findGroupByName(@PathVariable("name") String name) {
        return service.findByGroupName(name);
    }
    @PutMapping("/update_group/{name}/{newName}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновить данные учебной группы",
            description = "Обновляет уже существующего группу. Требует роль ADMIN.",
            tags = {"Groups"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Группа успешно обновлена",
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
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN), введенные данные некорректны",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public Group updateGroup(@PathVariable("name") String name, @PathVariable("newName") String newName) {
        return service.updateGroup(name, newName);
    }
    @DeleteMapping("/delete_group/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Удаление учебной группы",
            description = "Удаляет группу с её номеру, который берется из пути. Требуется роль ADMIN",
            tags = {"Groups"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление или группа не существовала"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)  или есть ссылки в disciplines не эту группу"
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
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
    @GetMapping("/position/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Найти должность по её названию",
            description = "Возвращает должность по её названию, которое берёт из пути /position/{name}. Требует роль ADMIN.",
            tags = {"Positions"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Должность найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Position.class)
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
    public Optional<Position> findPositionByName(@PathVariable("name") String name) {
        return service.findByPositionName(name);
    }
    @PutMapping("/update_position_{name}_{newName}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновить название должности",
            description = "Обновляет уже существующую должность. Требует роль ADMIN.",
            tags = {"Positions"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Должность успешно обновлена или такой должности не существует",
                            content = @Content(
                                    schema = @Schema(
                                            type = "string",
                                            example = """
                                                    ---------Вариант 1---------
                                                    Employee name successfully updated to newName.
                                                    ---------Вариант 2---------
                                                    There is no position name.
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
    public String updatePosition(@PathVariable("name") String name,
                                 @PathVariable("newName") String newName) {
        return service.updatePosition(name, newName);
    }
    @DeleteMapping("/delete_position/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Удаление должности по id",
            description = "Удаляет должность с указанным id, который берется из пути. Требуется роль ADMIN",
            tags = {"Positions"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление или должность не существовала"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)"
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
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
    @GetMapping("/discipline_find/{name}/{group}/{teacherID}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Найти дисциплину по ключу",
            description = "Поиск дисциплины по составному ключу (название, id группы, id преподавателя), который берёт из пути. Требует роль ADMIN.",
            tags = {"Disciplines"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Дисциплина найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DisciplineDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN), данные не корректны или дисциплина не найдена",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public DisciplineDTO findDisciplineByName(@PathVariable("name") String disciplineName,
                                              @PathVariable("group") Long groupID,
                                              @PathVariable("teacherID") Long teacherID)
    {
        DisciplinesKey dk = new DisciplinesKey(disciplineName, groupID, teacherID);
        Disciplines discipline = service.findByDisciplineKey(dk).orElseThrow(() -> new EntityNotFoundException("Discipline not found"));
        return DisciplineDTO.fromEntity(discipline);
    }
    @PutMapping("/update_discipline")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновить данные учебной дисциплины",
            description = "Обновляет уже существующую дисциплину. Требует роль ADMIN.",
            tags = {"Disciplines"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные дисциплины для обновления",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DisciplineUpdRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "disciplineName": "programming",
                                              "groupName": "23.Б11-ПУ",
                                              "teacherEmail": "Karen_Pagac75@yahoo.com",
                                              "countHours": 100,
                                              "newDisciplineName": "PROGRAMMING TEST",
                                              "newGroupName": "23.Б11-ПУ",
                                              "newTeacherEmail": "Karen_Pagac75@yahoo.com",
                                              "newCountHours": 100
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Дисциплина успешно обновлена или такой дисциплины не существует, не существует сотрудника или группы на которых она пытается сослаться при обновлении",
                            content = @Content(
                                    schema = @Schema(
                                            type = "string",
                                            example = """
                                                    ---------Вариант 1---------
                                                    Successfully update discipline PROGRAMMING TEST
                                                    ---------Вариант 2---------
                                                    There is no discipline <имя дисциплины, пытаются обновить>
                                                    ---------Вариант 3---------
                                                    Can not update. Teacher <teacher_email> not exist
                                                    ---------Вариант 4---------
                                                    Can not update. Group <group_name> not exist
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
    public String updateDiscipline(@RequestBody DisciplineUpdRequest request) {
        return service.updateDiscipline(request);
    }
    @DeleteMapping("/delete_discipline/{name}/{group}/{teacher}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Удаление дисциплины по ключу",
            description = "Удаляет дисциплину с указанным ключем (name, group, teacher), который берется из пути. Требуется роль ADMIN",
            tags = {"Disciplines"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление или дисциплина не существовала"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)"
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void deleteDiscipline(@PathVariable("name") String disciplineName,
                                 @PathVariable("group") Long groupID,
                                 @PathVariable("teacher") Long teacherID)
    {
        DisciplinesKey key = new DisciplinesKey(disciplineName, groupID, teacherID);
        service.deleteDiscipline(key);
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
    @GetMapping("/unit/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Найти подразделение ВУЗа его названию",
            description = "Возвращает подразделение по его названию, которое берёт из пути /unit/{name}. Требует роль ADMIN.",
            tags = {"Units"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Подразделение найдено",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Unit.class)
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
    public Optional<Unit> findUnitByName(@PathVariable("name") String name) {
        return service.findUnitByName(name);
    }
    @PutMapping("/update_unit")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновить данные подразделения ВУЗа",
            description = "Обновляет уже существующее подразделение. Требует роль ADMIN.",
            tags = {"Units"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные подразделения для обновления",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UnitUpdRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "oldName": "AM-CP faculty",
                                              "newName": "AM-CP faculty TEST",
                                              "newAddress": "test address"
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Подразделение успешно обновлено или такого подразделения не существует",
                            content = @Content(
                                    schema = @Schema(
                                            type = "string",
                                            example = """
                                                    ---------Вариант 1---------
                                                    Successfully update unit <oldName> to name <newName> at address <newAddress>
                                                    ---------Вариант 2---------
                                                    There is no unit <имя подразделения>, которое пытаются обновить>
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
    public String updateUnit(@RequestBody UnitUpdRequest request) {
        return service.updateUnit(request);
    }
    @DeleteMapping("/delete_unit/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Удаление подразделения ВУЗа по названию",
            description = "Удаляет подразделения ВУЗа с указанным названием, которое берется из пути. Требуется роль ADMIN",
            tags = {"Units"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление или подразделение не существовало"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)"
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
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
        return "ClassRoom " + created.getClassroomNumber() + " in unit " + created.getUnit().getUnitName() + " successfully saved";
    }
    @GetMapping("/classroom_find/{number}/{unit}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Найти аудиторию по ключу",
            description = "Поиск аудитории по составному ключу (номер аудитории, id подразделения), который берёт из пути. Требует роль ADMIN.",
            tags = {"ClassRooms"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Аудитория найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClassRoomRequest.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN), данные не корректны или аудитория не найдена",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ClassRoomRequest findClassRoomById(@PathVariable("number") String classroomNumber,
                                              @PathVariable("unit") Long unitID)
    {
        ClassRoomKey key = new ClassRoomKey(classroomNumber, unitID);
        ClassRoom classRoom = service.findClassRoomByID(key).orElseThrow(() -> new EntityNotFoundException("Classroom not found"));
        return ClassRoomRequest.fromEntity(classRoom);
    }
    @PutMapping("/update_classroom")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновить данные аудитории",
            description = "Обновляет уже существующую аудиторию. Требует роль ADMIN.",
            tags = {"ClassRooms"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные аудитории для обновления",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClassRoomUpdRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "old" : {"classroomNumber":"test aud",
                                                        "unitName":"Central unit"},
                                                "newNumber" : "test aud 1",
                                                "newCapacity" : 20
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Аудитория успешно обновлена или такой аудитории не существует",
                            content = @Content(
                                    schema = @Schema(
                                            type = "string",
                                            example = """
                                                    ---------Вариант 1---------
                                                    Successfully update classroom <newNumber>
                                                    ---------Вариант 2---------
                                                    There is no classroom <old number> in unit <unit name>
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
    public String updateClassRoom(@RequestBody ClassRoomUpdRequest request) {
        return service.updateClassRoom(request);
    }
    @DeleteMapping("/delete_classroom/{number}/{unit}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Удаление аудиторию по ключу",
            description = "Удаляет аудиторию с указанным ключем (number, unit), который берется из пути. Требуется роль ADMIN",
            tags = {"ClassRooms"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление или аудитория не существовала"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)"
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void deleteClassRoom(@PathVariable("number") String number,
                                @PathVariable("unit") Long unitID)
    {
        ClassRoomKey key = new ClassRoomKey(number, unitID);
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
    @GetMapping("/distance_find/{from}/{to}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Найти дистанцию по ключу",
            description = "Поиск дистанции (в минутах) между двумя подразделениями по составному ключу (подразделение А, подразделение В), который берёт из пути. Требует роль ADMIN.",
            tags = {"Distances"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Дистанция найдена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DistanceRequest.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN), данные не корректны или дистанция не найдена",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public DistanceRequest findDistanceById(@PathVariable("from") Long unitFrom,
                                            @PathVariable("to") Long unitTo) {
        DistanceKey key = new DistanceKey(unitFrom,unitTo);
        Distance dist = service.findDistanceByID(key).orElseThrow(() -> new EntityNotFoundException("Distance not found"));
        return DistanceRequest.fromEntity(dist);
    }
    @PutMapping("/update_distance")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновить дистанцию между подразделениями",
            description = "Обновляет уже существующую дистанцию. Требует роль ADMIN.",
            tags = {"Distances"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные дистанции для обновления",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DistanceRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "unitFrom" : "AM-CP faculty",
                                                "unitTo" : "Central unit",
                                                "time" : 95
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Дистанция успешно обновлена или таких подразделений не существует",
                            content = @Content(
                                    schema = @Schema(
                                            type = "string",
                                            example = """
                                                    ---------Вариант 1---------
                                                    Successfully update distance from <unitFrom> to <unitTo>
                                                    ---------Вариант 2---------
                                                    There is no unit <unitFrom/unitTo>
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
    public String updateDistance(@RequestBody DistanceRequest request) {
        return service.updateDistance(request);
    }
    @DeleteMapping("/delete_distance/{from}/{to}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Удаление дистанции по ключу",
            description = "Удаляет дистанции с указанным ключем (from, to), который берется из пути. Требуется роль ADMIN",
            tags = {"Distances"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление или дистанция не существовала"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)"
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void deleteDistance(@PathVariable("from") Long unitFrom,
                               @PathVariable("to") Long unitTo)
    {
        DistanceKey key = new DistanceKey(unitFrom, unitTo);
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
            description = """
                    Создает новое занятие или обновляет существующее. Требует роль ADMIN.
                    Список провеок:
                    - Корректность аудитории. Проверяется, что аудитория, в которой ставится занятие, может вместить всю группу, у которой ставится занятие.
                    - Проверка пересечения групп. Если ставится занятие у нескольких групп, также проверяется вместимость аудитории.
                    - Корректность времени. Проверяется, что занятие не ставится на то же время, на которое уже стоит занятие у той же группы.
                    - Проверка корректности подразделения. Администратор не сможет поставить соседние занятия в подразделениях вуза, путь между которыми занимает более, чем время между занятиями.
                    - Количество часов. В случае, если количество часов, рассчитанных программой на неделю, исходя из сохраненного в базе количества часов на семестр будет меньше/больше того, что администратор добавил - программа будет высвечивать предупреждение.
                    """,
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
                                                 "disciplineID": "1",
                                                 "classRoomID" : "2",
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
                                                    Lesson successfully saved! <опционально: данные о том больше или меньше занятий, относительно учебного плана добавлено>
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
            return "Lesson successfully saved!\n"
                    + createLog;
        }
        return createLog;
    }
    @GetMapping("/lesson_find/{disciplineID}/{classRoomID}/{date}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Найти занятие по ключу",
            description = "Поиск занятия по составному ключу (ID дисциплины, ID класса, дата проведения), который берёт из пути. Требует роль ADMIN."
            +"\nпример: localhost:8080/api/v1/lesson_find/1/2/2025-10-22T09:30:00.000+03:00",
            tags = {"Schedule"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Занятие найдено",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LessonRequest.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN), данные не корректны или занятие не найдено",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public LessonRequest findLessonById(@PathVariable("disciplineID") Long disciplineID,
                                        @PathVariable("classRoomID") Long classRoomID,
                                        @PathVariable("date") String date)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date d;
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        LessonRequest request = new LessonRequest(
                disciplineID,
                classRoomID,
                d
        );
        Lesson lesson = service.findLessonByID(request).orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
        return LessonRequest.fromEntity(lesson);
    }
    @GetMapping("/find_lesson_ondate/{date}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Найти занятия по дате",
            description = "Поиск списка занятий по дате проведения, которая берётся из пути. Требует роль ADMIN."
                    +"\nпример: localhost:8080/api/v1/find_lesson_ondate/2025-10-22",
            tags = {"Schedule"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Занятия найдено",
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
                            description = "Недостаточно прав (требуется роль ADMIN), данные не корректны",
                            content = @Content
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public List<LessonRequest> findLessonsByDate(@PathVariable("date") String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        DateRequest request = new DateRequest();
        request.setDate(d);
        return service.findLessonsByDate(request.getDate()).stream().map(LessonRequest::fromEntity).collect(Collectors.toList());
    }
    @PutMapping("/update_lesson")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновить данные о занятии в расписании",
            description = "Обновляет уже добавленное ранее занятие. Требует роль ADMIN. При обновлении осуществляются те же проверки, что при добавлении.",
            tags = {"Schedule"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные занятия для обновления",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LessonUpdRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                 "disciplineID": 1,
                                                 "classRoomID": 2,
                                                 "date" : "2025-11-04T17:30:00.000+03:00",
                                                 "newDisciplineID": 2,
                                                 "newClassRoomID": 2,
                                                 "newDate" : "2025-11-04T09:30:00.000+03:00"
                                             }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запись в расписании успешно обновлена или новые параметры не прошли проверки",
                            content = @Content(
                                    schema = @Schema(
                                            type = "string",
                                            example = """
                                                    ---------Вариант 1---------
                                                    Lesson successfully updated.
                                                    ---------Вариант 2---------
                                                    Cannot update lesson with causes: <список причин>
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
    public String updateLesson(@RequestBody LessonUpdRequest request) {
        String createLog = service.updateLesson(request);
        if (!createLog.startsWith("Cannot update lesson with causes:")) {
            return "Lesson successfully updated!\n"
                    + createLog;
        }
        return createLog;
    }
    @DeleteMapping("/delete_lesson/{disciplineID}/{classRoomID}/{date}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Удаление занятия по ключу",
            description = "Удаляет дисциплину с указанным ключем (disciplineID, classRoomID, date), который берется из пути. Требуется роль ADMIN",
            tags = {"Schedule"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление или такой записи в расписании не было"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)"
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void deleteLesson(@PathVariable("disciplineID") Long disciplineID,
                             @PathVariable("classRoomID") Long classRoomID,
                             @PathVariable("date") String date)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date d;
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        LessonRequest request = new LessonRequest(
                disciplineID,
                classRoomID,
                d
        );
        service.deleteLesson(request);
    }
    @DeleteMapping("/delete_all_lessons")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Удаление всех занятий",
            description = "Очищает таблицу с расписанием. Требуется роль ADMIN",
            tags = {"Schedule"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное удаление"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Недостаточно прав (требуется роль ADMIN)"
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void deleteAllLessons() {
        service.deleteAllLessons();
    }

}